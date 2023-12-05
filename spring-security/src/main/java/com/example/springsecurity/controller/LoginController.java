package com.example.springsecurity.controller;

import com.example.springsecurity.common.R;
import com.example.springsecurity.model.MyUser;
import com.example.springsecurity.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/user/login")
    public R login(@RequestBody MyUser user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", loginService.login(user));
        return R.ok().data(map);
    }

    @RequestMapping("/home")
    public String success(){
        return "success";
    }
}