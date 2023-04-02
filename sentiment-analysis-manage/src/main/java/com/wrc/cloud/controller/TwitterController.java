package com.wrc.cloud.controller;

import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.PO.TwitterUserPO;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.TwitterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

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

    @PostMapping("/comment")
    public ResponseResult<Integer> add(@RequestBody TweetPO tweet) {
//        log.info(tweet.toString());
        int result = twitterService.insertTweet(tweet);
        return ResponseResult.success(result);
    }

    /**
     * 返回 时间+@用户名 格式的键值
     * */
    @GetMapping("/comment/whitelist")
    public ResponseResult<List<String>> getWhiteListConversation(){
        Date currentTime = new Date();
        // 20个小时之内不再爬取
        Date startTime = new Date(currentTime.getTime() - 1000*3600*24);
        List<TweetPO> conversations = twitterService.getConversationsByTime(startTime, currentTime);

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
    public ResponseResult<Long> getComments(@Param("key") String key) throws ParseException {
        List<String> list = new LinkedList<>();
        list.add(key);

        String beginTime = "2023-03-01 14:42:32";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = format.parse(beginTime);
        Date date2 = new Date();
        List<TweetPO> result = twitterService.getTweetsByTimeAndKeyWords(date1, date2, list);
        System.out.println("result===============================================");
        for (TweetPO tweet : result){
            System.out.println(tweet.getFullText());
        }
        return ResponseResult.success(1L);
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

