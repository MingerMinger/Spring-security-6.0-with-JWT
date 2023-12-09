package com.example.springsecurity.controller;

import cn.hutool.core.util.RandomUtil;
import com.example.springsecurity.common.R;
import com.example.springsecurity.common.ResponseEnum;
import com.example.springsecurity.mapper.UserMapper;
import com.example.springsecurity.model.MyUser;
import com.example.springsecurity.service.cacheService.CodeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
public class RegisterController {
    @Autowired
    private CodeCache codeCache;
    @Autowired
    private UserMapper userMapper;

    // 这个是 mail 依赖提供给我们的发送邮件的接口
    @Autowired
    private JavaMailSender mailSender;

    // 获取发件人邮箱
    @Value("${spring.mail.username}")
    private String sender;

    // 获取发件人昵称
    @Value("${spring.mail.nickname}")
    private String nickname;


    //获取邮箱验证码
    @GetMapping("/user/register/code")
    public R registerCode(@RequestParam("username") String username) {
        if (!StringUtils.hasText(username)) {
            return R.error("用户名不能为空！");
        }
        String emailRegex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        boolean emailSuccess = Pattern.matches(emailRegex, username);
        if (!emailSuccess) {
            return R.error("邮箱格式不正确！");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(nickname + '<' + sender + '>');
        message.setTo(username);
        message.setSubject("欢迎访问专升本系统！");
        //        验证码随机值
        String code = RandomUtil.randomString(6);
        codeCache.putLoginCode(username, code);

        String content = "【验证码】您的验证码为：" + code + " 。 验证码三分钟内有效，逾期作废。\n\n\n";

        message.setText(content);

        mailSender.send(message);
        return R.ok("邮箱获取成功！");
    }

    //注册
    @PostMapping("/user/register")
    public R register(@RequestBody RegDto regDto) {
        if (!StringUtils.hasText(regDto.username) || !StringUtils.hasText(regDto.password)) {
            return R.error("账号或者密码不能为空！");
        }
        // 邮箱格式验证，密码格式验证
        String emailRegex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        boolean emailSuccess = Pattern.matches(emailRegex, regDto.username);
        if (!emailSuccess) {
            return R.error("邮箱格式不正确！");
        }
        String passwordRegex = "^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,16}$/;\n";
        boolean pwdSuccess = Pattern.matches(passwordRegex, regDto.password);
        if (!pwdSuccess) {
            return R.error("包含大小写字母、数字、特殊字符至少3个组合大于8小于16个字符");
        }

//        验证码验证
        if (!StringUtils.hasText(regDto.validateCode)) {
            return R.setResult(ResponseEnum.CODE_NULL_ERROR);
        }
        String code = codeCache.getSMSCode(regDto.username,null);
        if (code != null && !code.equals(regDto.validateCode)) {
            return R.setResult(ResponseEnum.CODE_ERROR);
        }
        //存储到数据库
        MyUser myUser = new MyUser();
        myUser.setUsername(regDto.username);
        myUser.setPassword(regDto.password);
        myUser.setEnabled(1);
        myUser.setRoles("User");
        userMapper.insertOne(myUser);
        codeCache.removeSMSCode(regDto.username);
        return R.ok("注册成功！");
    }

    record RegDto(String username, String password, String validateCode) {
    }
}
