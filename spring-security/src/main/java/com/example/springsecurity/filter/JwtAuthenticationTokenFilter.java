package com.example.springsecurity.filter;

import com.alibaba.fastjson.JSON;
import com.example.springsecurity.model.LoginUser;
import com.example.springsecurity.model.MyUser;
import com.example.springsecurity.service.cacheService.LoginCacheService;
import com.example.springsecurity.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    LoginCacheService cacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        // 如果携带了token, 就做一系列操作, 否则直接放行
        if (StringUtils.hasText(token)) {
            // 解析token
            String userId;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userId = claims.getSubject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("token 非法!");
            }

            // 从Redis中获取用户信息
            // todo 将User（实体）和UserDetails(loginUser)分开，放入缓存里的是user
            MyUser user = JSON.
                    parseObject(cacheService.cacheLoginUser("loginUSer-"+userId,null),MyUser.class);
            if (ObjectUtils.isEmpty(user)) {
                throw new RemoteException("用户未登陆!");
            }
            LoginUser loginUser = new LoginUser(user);
            // 存入SecurityContextHolder, 因为后面的filter都是从SecurityContextHolder获取的
            // todo 获取权限列表, 封装到authenticationToken
            List<GrantedAuthority> authoritis = null;
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, authoritis);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 放行
        filterChain.doFilter(request, response);
    }


}