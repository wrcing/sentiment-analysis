package com.wrc.cloud.bean;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/3/28 17:45
 */

//@Component
//@RabbitListener(queues = "TestDirectQueue")
public class MQDirectReceiver {

//    @RabbitHandler
//    public void process(Map testMessage) {
//        System.out.println("DirectReceiver 1 消费者收到消息  : " + testMessage.toString());
//    }

}
