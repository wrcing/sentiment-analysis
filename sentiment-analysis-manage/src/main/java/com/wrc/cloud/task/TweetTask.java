package com.wrc.cloud.task;

import cn.hutool.core.date.DateUtil;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.service.TwitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/10 9:24
 */
@Component
@Slf4j
public class TweetTask {

    @Resource
    private TwitterService twitterService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private static Map<String, LinkedList<String>> key_texts = new HashMap<String, LinkedList<String>>(){{
        put("btc", new LinkedList<String>(){{
            add("btc");
            add("bitcoin");
        }});

        put("三上", new LinkedList<String>(){{
            add("三上悠亜");
            add("悠亜");
            add("yua");
        }});
    }};

    /**
     * 查出没有分析结果的tweet
     * 将信息发送至MQ
     * */
    @Scheduled(cron = "0 16 15 * * ?")
    public void sendTweetMessageToMQ(){
        Date startCreateTime = DateUtil.parse("2022-12-12 00:00:00");
        Date endCreateTime = new Date();

        // 一周一周的处理
        while (startCreateTime.before(endCreateTime)){
            Date tmpEndTime = new Date(startCreateTime.getTime() + 1000 * 3600 * 24 * 7);
            List<TweetPO> tweets = twitterService.getTweetsWithoutAnalysis(startCreateTime, tmpEndTime);
            for (TweetPO tweet : tweets){
                sendToEnAnalysisQueue(tweet);
            }
            log.info("未分析tweet数量: "+String.valueOf(tweets.size())+ " "
                    + DateUtil.format(startCreateTime, "yyyy-MM-dd HH:mm:ss.SSS")
                    + " --- "
                    + DateUtil.format(tmpEndTime, "yyyy-MM-dd HH:mm:ss.SSS"));

            startCreateTime = tmpEndTime;
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

    /**
     * 通过预设的key 查出tweet，并给每个查出的tweet打上key的标签
     * */
    @Scheduled(cron = "0 21 11 * * ?")
    public void updateTweetKeys(){
        Date startCreateTime = DateUtil.parse("2022-12-20 13:46:27");
        Date endCreateTime = DateUtil.parse("2023-04-01 13:46:27");

        // 一天一天的处理
        while (startCreateTime.before(endCreateTime)){
            Date tmpEndTime = new Date(startCreateTime.getTime() + 1000 * 3600 * 24);
            updateTweetKeyDuringTimeSegment(startCreateTime, tmpEndTime);
            startCreateTime = tmpEndTime;
        }

    }

    private void updateTweetKeyDuringTimeSegment(Date startCreateTime, Date endCreateTime){
        Set<BigInteger> existedId = new HashSet<>();
        for (Map.Entry<String, LinkedList<String>> item : key_texts.entrySet()){
            // 根据texts查出tweet
            List<TweetPO> tweets = twitterService.getTweetsByTimeAndTexts(startCreateTime, endCreateTime, item.getValue());
            // 保存对应的key
            for(TweetPO tweet : tweets){
                if (!existedId.contains(tweet.getConversationId())){
                    if (twitterService.countKeyWithConversationTweetId(item.getKey(), tweet.getConversationId()) == 0){
                        // 数据库中无数据
                        int result = twitterService.saveConversationTweetKeyById(item.getKey(), tweet.getConversationId());
                    }
                    existedId.add(tweet.getConversationId());
                }
            }
        }
        log.info("已添加 "+ String.valueOf(existedId.size()) + " 条对话的关键词, "
                + DateUtil.format(startCreateTime, "yyyy-MM-dd HH:mm:ss.SSS")
                + " --- "
                + DateUtil.format(endCreateTime, "yyyy-MM-dd HH:mm:ss.SSS")
                );
    }
}
