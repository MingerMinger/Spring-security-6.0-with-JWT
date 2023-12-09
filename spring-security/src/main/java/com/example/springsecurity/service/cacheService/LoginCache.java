package com.example.springsecurity.service.cacheService;

import com.alibaba.fastjson.JSON;
import com.example.springsecurity.model.LoginUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class LoginCache {
    //获取登录用户
    @Cacheable(key = "'loginUser-'+ #id", cacheNames = "login")
    public String cacheLoginUser(String id, LoginUser loginUser) {
        if (!Objects.isNull(loginUser)) {
            return JSON.toJSONString(loginUser.user());
        }
        return null;
    }

    // 存储
    @CachePut(key = "'loginUser-'+ #id", cacheNames = "login")
    public String putCacheLoginUser(String id, LoginUser loginUser) {
        if (!Objects.isNull(loginUser)) {
            return JSON.toJSONString(loginUser.user());
        }
        return null;
    }

    @CacheEvict(key = "'loginUser-'+ #id", cacheNames = "login")
    public void removeLoginUser(String id) {

    }

}
