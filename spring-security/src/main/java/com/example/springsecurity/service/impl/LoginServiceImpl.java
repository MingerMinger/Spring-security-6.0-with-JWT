package com.example.springsecurity.service.impl;

import com.example.springsecurity.model.LoginUser;
import com.example.springsecurity.model.MyUser;
import com.example.springsecurity.service.LoginService;
import com.example.springsecurity.service.cacheService.LoginCache;
import com.example.springsecurity.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    LoginCache loginCache;

    @Override
    public String login(MyUser user) {
        // AuthenticationManager 的authenticate()方法进行身份认证
        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 如果认证没通过
        if (ObjectUtils.isEmpty(authenticate)) {
            throw new RuntimeException("账号或密码错误!!!");
        }

        // 验证通过, 获取到loginUser, 使用userId 生产一个jwt返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String jwt = JwtUtil.createJWT(loginUser.user().getId().toString());

        // 将用户信息存入ehcache,可以使用redis
        loginCache.putCacheLoginUser("loginUser-" + loginUser.user().getId(), loginUser);

        return jwt;
    }
}

