package com.wrc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/23 22:46
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableDiscoveryClient
public class GateWayMain9527 {
    public static void main(String[] args) {
        SpringApplication.run(GateWayMain9527.class, args);
    }
}
