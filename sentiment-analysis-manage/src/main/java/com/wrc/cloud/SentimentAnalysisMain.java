package com.wrc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/30 14:14
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableTransactionManagement
public class SentimentAnalysisMain {
    public static void main(String[] args) {
        SpringApplication.run(SentimentAnalysisMain.class, args);
    }
}
