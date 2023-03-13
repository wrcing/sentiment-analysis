package com.wrc.cloud.test;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/24 10:03
 */
public class Test9527 {

    @Test
    public void testDate(){
        ZonedDateTime zbj = ZonedDateTime.now(); // 默认时区
        System.out.println(zbj);
    }
}
