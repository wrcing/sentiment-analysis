package com.wrc.cloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/6 21:25
 */
@Configuration
@MapperScan("com.wrc.cloud.dao")
public class MybatisConfig {
}
