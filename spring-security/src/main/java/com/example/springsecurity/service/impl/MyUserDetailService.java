package com.example.springsecurity.service.impl;

import com.example.springsecurity.mapper.UserMapper;
import com.example.springsecurity.model.LoginUser;
import com.example.springsecurity.model.MyUser;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MyUserDetailService implements UserDetailsService {


    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        MyUser user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("账号不存在！！");
        }
        //从实体类中获取用户的密码。
        String password = user.getPassword();

//        List<GrantedAuthority> grantedAuthorityList = (List<GrantedAuthority>) user.getAuthorities();
//        //获取用户角色。


        //收集角色和权限，一个用户可以对应多个角色，一个角色可以对应多个权限（比如访问某个菜单或方法的权限）
        //你可以定义一个角色表，一个权限表。同时要建一个用户到角色的关联表和角色到权限的关联表。这些比较简单，这里不再赘述。
//        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
//        //获取用户角色。
//        List<Role> roles = userService.loadRoleByUid(user.getId());
//        for(Role role:roles){
//            //角色前一定要加上"ROLE_"前缀，否则SpringSecurity会视为无效，它是通过这个前缀识别角色的。
//            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_".concat(role.getName()));
//            grantedAuthorityList.add(simpleGrantedAuthority);
//            //System.out.println(role.getName());
//        }
        //获取用户各模块或菜单的权限。

        String roles = userMapper.findRoleByUsername(username);

        // 获取权限列表，根据数据库表的设计来获取，这里不实现权限管理
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles.split(",")) {
            authorities.add(new SimpleGrantedAuthority(role.trim()));
        }

        return new LoginUser(user);
    }
}
