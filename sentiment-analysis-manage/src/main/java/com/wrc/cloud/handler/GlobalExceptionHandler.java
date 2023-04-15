package com.wrc.cloud.handler;


import com.wrc.cloud.entities.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/15 13:52
 */
//@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理druid 违反约束，像重复主键
     * 但druid的异常这里抓不到？
     * */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseResult<String> bindExceptionHandler(SQLIntegrityConstraintViolationException e) {
        // com.wrc.cloud.service.impl.TwitterServiceImpl.insertTwitterUser
        log.error(e.getMessage());
        log.error(e.getLocalizedMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        if (false){
            return ResponseResult.success("重复twitter user");
        }
        return ResponseResult.error("bad service");
    }
}
