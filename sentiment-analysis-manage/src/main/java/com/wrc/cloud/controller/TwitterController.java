package com.wrc.cloud.controller;

import cn.hutool.core.date.DateUtil;
import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.PO.TwitterUserPO;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.TwitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/3/29 16:53
 */

@Slf4j
@RestController
@RequestMapping("/api/twitter")
public class TwitterController {

    @Resource
    private TwitterService twitterService;

    private static final Long MAX_TIME_MILLION_SECONDS = DateUtil.parse("3333-12-12 00:00:00").getTime();
    private static final Long MILLION_SECONDS_OF_ON_DAY = 24*3600*1000L;

    @PostMapping("/comment")
    public ResponseResult<Integer> add(@RequestBody TweetPO tweet) {
//        log.info(tweet.toString());
        int result = twitterService.insertTweet(tweet);
        return ResponseResult.success(result);
    }

    /**
     * 返回 时间+@用户名 格式的键值
     *
     * 目前还不需要cache，也没迁移进service
     * 暂时将逻辑放于此
     * 如若插件用户增多，可以考虑，但这种自动刷新的功能应该只有rc自己用了，那就没必要再优化了
     * */
    @GetMapping("/comment/whitelist")
    public ResponseResult<List<String>> getWhiteListConversation(@RequestParam(name = "startTime", required = false) Long start,
                                                                 @RequestParam(name = "endTime", required = false) Long end){
        Date startTime = start == null ? DateUtil.parse("2022-12-12 14:42:32") : new Date(start);
        Date endTime = end == null ? new Date() : new Date(end);
        List<TweetPO> conversations = twitterService.getConversationsByTime(startTime, endTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        List<String> result = new LinkedList<>();
        for (TweetPO conversation : conversations){
            TwitterUserPO user = twitterService.getUserById(conversation.getUserId());
            result.add(dateFormat.format(conversation.getCreatedAt()) + "@" + user.getScreenName());
        }

        return ResponseResult.success(result);
    }


    /**
     * 返回已有数据量
     * */
    @GetMapping("/comment/count")
    public ResponseResult<Long> getCount(){
        Long countTweet = twitterService.countTweet();
        return ResponseResult.success(countTweet);
    }


    /**
     * 返回所有符合 主体和时间条件 的tweet
     * */
    @GetMapping("/analysis")
    public ResponseResult<Long> getComments(@RequestParam("key") String key) throws ParseException {
        List<String> list = new LinkedList<>();
        list.add(key);

        String beginTime = "2023-03-01 14:42:32";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = format.parse(beginTime);
        Date date2 = new Date();
        List<TweetPO> result = twitterService.getTweetsByTimeAndTexts(date1, date2, list);
        System.out.println("result===============================================");
        for (TweetPO tweet : result){
            System.out.println(tweet.getFullText());
        }
        return ResponseResult.success(1L);
    }

    /**
     * 返回所有符合 主体和时间条件 的tweet分析 的统计数据
     *
     * 最小时间间隔 半个小时
     * */
    @GetMapping("/analysis/statistic")
    public ResponseResult<Map<String, Long>> getAnalysisStatisticByKeyAndTime(@RequestParam("keys") List<String> keys,
                                                                              @RequestParam("timePoint") Long timePoint,
                                                                              @RequestParam("preSeconds") Long seconds) {
        if (keys == null) keys = new LinkedList<>();
        // 以分钟为间隔单位
        timePoint = ((timePoint == null ? new Date().getTime() : timePoint) /1000 /60) * 60*1000L;
        if (MAX_TIME_MILLION_SECONDS.compareTo(timePoint) < 0) {
            return ResponseResult.error("timePoint超出限制");
        }
        // 最小1800秒
        seconds = Long.max(1800L, seconds == null ? 0L : seconds);

        if (seconds.compareTo(3600L*24) >= 0){
            // 1 day 以上，通过分每日小片查，不同查询间可以重复使用缓存
            Date startTime = new Date(timePoint - seconds * 1000);
            Date endTime = new Date(timePoint);
            // 开始的一小段，到当日的24：00
            Date tmpEnd = new Date(startTime.getTime()/MILLION_SECONDS_OF_ON_DAY*MILLION_SECONDS_OF_ON_DAY
                    +MILLION_SECONDS_OF_ON_DAY);
            Map<String, Long> result = new HashMap<>();
            // 一日为一个单位,00:00-->00:00，(百京时间8:00-->8:00)两头的小段单独查
            while (startTime.before(endTime)){
                Map<String, Long> tmpResult = twitterService.getAnalysisStatisticByKeyAndTime(
                        keys,
                        tmpEnd,
                        (tmpEnd.getTime() - startTime.getTime()) / 1000);
                for (Map.Entry<String, Long> item : tmpResult.entrySet()){
                    Long baseValue = result.get(item.getKey());
                    result.put(item.getKey(), (baseValue == null ? 0L: baseValue) + item.getValue());
                }

                startTime.setTime(tmpEnd.getTime());
                tmpEnd.setTime(Math.min(tmpEnd.getTime() + MILLION_SECONDS_OF_ON_DAY, endTime.getTime()));
            }
            return ResponseResult.success(result);
        }
        // 小范围的单个查询，正常逻辑
        Map<String, Long> statisticCount = twitterService.getAnalysisStatisticByKeyAndTime(
                keys,
                new Date(timePoint),
                seconds);
        return ResponseResult.success(statisticCount);
    }


    /**
     * twitter 用户管理
     * */
    @PostMapping("/user")
    public ResponseResult<Integer> addUser(@RequestBody TwitterUserPO user) {
//        log.info(user.toString());
        int result = twitterService.insertTwitterUser(user);
        return ResponseResult.success(result);
    }
}

