package com.example.springsecurity.service.cacheService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class CodeCache {

    // 存储登录Code
    @CachePut(key = "'loginUser-'+ #id", cacheNames = "code")
    public String putLoginCode(String id, String code) {
        return code;
    }


    // 清楚登录验证码缓存
    @CacheEvict(key = "'loginUser-'+ #id", cacheNames = "code")
    public void removeLoginCode(String id) {

    }

    // 存储注册SMSCode
    @CachePut(key = "#username", cacheNames = "code")
    public String putSMSCode(String username, String code) {
        return code;
    }

    // 清楚注册验证码缓存
    @CacheEvict(key = "#username", cacheNames = "code")
    public void removeSMSCode(String username) {

    }

    // 获取缓存里的验证码
    @Cacheable(key = "#username", cacheNames = "code")
    public String getSMSCode(String username,String code){
        return code;
    }
}
