package com.wrc.cloud.task;

import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.service.TwitterService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/10 9:24
 */
@Component
public class TweetAnalysisTask {

    @Resource
    private TwitterService twitterService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 查出没有分析结果的tweet
     * 将信息发送至MQ
     * */
    @Scheduled(cron = "0 0 2 * * ?")
    public void sendTweetMessageToMQ(){
        // beijing 2023-01-01 00:00:00
        Date startCreateTime = new Date(1672502400000L);
        Date endCreateTime = new Date();
        List<TweetPO> tweets = twitterService.getTweetsWithoutAnalysis(startCreateTime, endCreateTime);

        System.out.println("tweets without analysis num: "+String.valueOf(tweets.size()));
        for (TweetPO tweet : tweets){
            sendToEnAnalysisQueue(tweet);
        }
    }

    private void sendToEnAnalysisQueue(TweetPO tweet){
        Map<String, Object> map = new HashMap<>();
        map.put("siteId", AnalysisPO.TWITTER_SITE_ID);
        map.put("id", tweet.getId());

        //将消息携带绑定键值：TestDirectRouting 发送到交换机Cloud.AnalysisExchange
        rabbitTemplate.convertAndSend(
                "Cloud.AnalysisExchange",
                "AnalysisOrderRouteEN",
                map);
    }
}
