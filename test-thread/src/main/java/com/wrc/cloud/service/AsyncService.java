package com.wrc.cloud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/31 12:51
 */
@Service
@Slf4j
public class AsyncService {

    /**
     * 执行异步任务
     */
    @Async("asyncServiceExecutor")
    public void executeAsync(){
        log.info("start executeAsync");
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }
        log.info("end executeAsync");
    }
}
