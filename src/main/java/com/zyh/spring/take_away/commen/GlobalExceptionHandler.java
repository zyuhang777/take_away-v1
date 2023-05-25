package com.zyh.spring.take_away.commen;

import com.zyh.spring.take_away.commen.Exception.TakeAwayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exception(SQLIntegrityConstraintViolationException e){
        log.info("{}",e);
        if(e.getMessage().contains("Duplicate entry")){
            String[] EeMessage = e.getMessage().split(" ");
            String reStr = EeMessage[2] + "已经存在了";
            return R.error(reStr);
        }
        return R.error("添加失败（未知错误）");
    }
    @ExceptionHandler(TakeAwayException.class)
    public R<String> takeAway(TakeAwayException takeAwayException){
        log.info("{}",takeAwayException);
        return R.error(takeAwayException.getMessage());
    }
}
