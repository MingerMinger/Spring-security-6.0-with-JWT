package com.example.springsecurity.controller;

import com.example.springsecurity.common.R;
import com.example.springsecurity.exception.ParamsException;
import com.example.springsecurity.model.MyUser;
import com.example.springsecurity.service.LoginService;
import com.example.springsecurity.service.cacheService.CodeCache;
import com.example.springsecurity.service.cacheService.LoginCache;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.regex.Pattern;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private LoginCache loginCacheService;
    @Autowired
    private CodeCache codeCache;


    @PostMapping("/user/login")
    public R login(@RequestBody MyUser user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", loginService.login(user));
        return R.ok().data(map);
    }

    @GetMapping("/logout")
    public R logout(@RequestParam("userId") String userId) {
        if (!StringUtils.hasText(userId)) {
            return R.error("用户名不能为空！");
        }
        // 清楚登录缓存
        loginCacheService.removeLoginUser(userId);

        return R.ok("登出成功!");
    }


    // 验证码图片生成
    @GetMapping("/kaptcha")
    public void Kaptcha(@RequestParam("username") String username,
                        HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse)
            throws Exception {
        if (!StringUtils.hasText(username)) {
            throw new ParamsException("用户名不能为空！");
        }
        String emailRegex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        boolean emailSuccess = Pattern.matches(emailRegex, username);
        if (!emailSuccess) {
            throw new ParamsException("邮箱格式不正确！");
        }


        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/gif");

        //生成验证码对象,三个参数分别是宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
        //设置验证码的字符类型为数字和字母混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        // 设置内置字体
        captcha.setCharType(Captcha.FONT_1);
        //验证码存入cache
        codeCache.putLoginCode(username, captcha.text().toLowerCase());
        //输出图片流
        captcha.out(httpServletResponse.getOutputStream());
    }
}