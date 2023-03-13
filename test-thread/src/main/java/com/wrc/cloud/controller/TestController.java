package com.wrc.cloud.controller;

import com.wrc.cloud.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/31 12:56
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private AsyncService asyncService;

    @RequestMapping("/hello")
    public String submit(){
        log.info("start submit");

        //调用service层的任务
        asyncService.executeAsync();

        log.info("end submit");

        return "success";
    }
}
