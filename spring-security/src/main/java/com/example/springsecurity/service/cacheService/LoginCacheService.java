package com.example.springsecurity.service.cacheService;

import com.alibaba.fastjson.JSON;
import com.example.springsecurity.model.LoginUser;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginCacheService {
    @Cacheable(key = "'loginUser-'+ #id", cacheNames = "login")
    public String cacheLoginUser(String id ,LoginUser loginUser) {
        if (Objects.isNull(loginUser)){
            return null;
        }
        return JSON.toJSONString(loginUser.user());
    }

}
