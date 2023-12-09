package com.example.springsecurity.handler;

import com.example.springsecurity.common.R;
import com.example.springsecurity.exception.ParamsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public R exceptionHandler(ParamsException pe) {
        log.error(pe.getMessage());
        return R.error(pe.getMessage());
    }

}
