package com.wrc.cloud.controller.test;

import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.entities.ResponseResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/3/28 16:35
 */
@RestController
@RequestMapping("/api/mq")
public class RabbitMQTestController {

    //使用RabbitTemplate,这提供了接收/发送等等方法
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendmsg")
    public ResponseResult<String> sendDirectMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("siteId", AnalysisPO.WEIBO_SITE_ID);
        map.put("id", "4802490992951446");


        //将消息携带绑定键值：TestDirectRouting 发送到交换机Cloud.AnalysisExchange
        rabbitTemplate.convertAndSend("Cloud.AnalysisExchange", "AnalysisOrderRoute",
                map);

        return ResponseResult.success("ok");
    }

}
