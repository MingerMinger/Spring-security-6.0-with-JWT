package com.example.springsecurity.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ergwang
 * @date 2022/5/20 1:55 PM
 */
@Data
public class R {
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    /**
     * 构造函数私有化
     */
    private R() {
    }


    /**
     * 请求成功
     */
    public static R ok() {
        R result = new R();
        result.setCode(ResponseEnum.SUCCESS.getCode());
        result.setMessage(ResponseEnum.SUCCESS.getMessage());
        return result;
    }

    /**
     * 请求成功, 自定义消息
     */
    public static R ok(String message) {
        R result = new R();
        result.setCode(ResponseEnum.SUCCESS.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 请求失败
     */
    public static R error() {
        R result = new R();
        result.setCode(ResponseEnum.ERROR.getCode());
        result.setMessage(ResponseEnum.ERROR.getMessage());
        return result;
    }

    /**
     * 请求失败, 自定义消息
     */
    public static R error(String message) {
        R result = new R();
        result.setCode(ResponseEnum.ERROR.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 设置自定义返回结果
     */
    public static R setResult(ResponseEnum responseEnum) {
        R result = new R();
        result.setCode(responseEnum.getCode());
        result.setMessage(responseEnum.getMessage());
        return result;
    }


    /**
     * 自定义code
     */
    public R code(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * 自定义message
     */
    public R message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置data
     */
    public R data(String key, Object val) {
        this.data.put(key, val);
        return this;
    }

    /**
     * 设置data, data本身就是map
     */
    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}
