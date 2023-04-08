package com.wrc.cloud.service.impl;

import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.PO.TwitterUserPO;
import com.wrc.cloud.dao.TwitterDao;
import com.wrc.cloud.service.TwitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/1 14:11
 */
@Service
@Slf4j
public class TwitterServiceImpl implements TwitterService {

    @Resource
    private TwitterDao twitterDao;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public int insertTweet(TweetPO tweet) {
        tweet.setCatchTime(new Date());
        TweetPO existedTweet = twitterDao.queryTweetById(tweet.getId());
        int result = -1;
        if (existedTweet == null){
            result = twitterDao.insertTweet(tweet);
            if (result != 1){
                log.info("tweet insert错误: "+tweet.getUrl());
            }
            else {
                Map<String, Object> map = new HashMap<>();
                map.put("siteId", AnalysisPO.TWITTER_SITE_ID);
                map.put("id", tweet.getId());
                rabbitTemplate.convertAndSend(
                        "Cloud.AnalysisExchange",
                        "AnalysisOrderRouteEN", map);
            }
        }
        else {
            result = 0;
            if (tweet.getId().equals(tweet.getConversationId())){
                //  id与url后缀不同，那便是广告
                if (tweet.getUrl().endsWith(tweet.getId().toString())){
                    log.info("重复tweet："+tweet.getUrl());
                }
//                else{
//                    log.info("广告: "+tweet.getId().toString()+" url: "+tweet.getUrl() );
//                }
            }
        }


        return result;
    }

    @Override
    public TweetPO queryTweetById(BigInteger id) {
        return twitterDao.queryTweetById(id);
    }

    @Override
    public List<AnalysisPO> getAllAnalysisByUrl(String url) {
        TweetPO mainTweet = twitterDao.queryTweetByUrl(url);
        // 通过主贴的 id 查询所有该贴下的 分析结果
        return twitterDao.queryAnalysisByMainTweetId(mainTweet.getId());
    }

    /**
     * keyWords 应该全为小写
     * */
    @Override
    public List<TweetPO> getTweetsByTimeAndKeyWords(Date startTime, Date endTime, List<String> keyWords) {
        if (!startTime.before(endTime)){
            return new LinkedList<>();
        }
        //  若要查询某时间段所有的tweet，传一个[""]即可
        if(keyWords == null || keyWords.isEmpty()){
            return new LinkedList<>();
        }
        // 转化为小写
        for (int i = 0; i < keyWords.size(); i++){
            keyWords.set(i, keyWords.get(i).toLowerCase());
        }
        return twitterDao.queryTweetsByTextAndTime(startTime, endTime, keyWords);
    }

    @Override
    public List<TweetPO> getConversationsByTime(Date startTime, Date endTime) {
        return twitterDao.queryConversationIdsByTime(startTime, endTime);
    }

    @Override
    public Long countTweet() {
        return twitterDao.countTweet();
    }

    @Override
    public int insertAnalysis(AnalysisPO analysis) {
        analysis.setCreatedAt(new Date());
        analysis.setSiteId(AnalysisPO.TWITTER_SITE_ID);
        if (twitterDao.queryAnalysisById(analysis.getId()) != null){
            return twitterDao.updateAnalysis(analysis);
        }
        return twitterDao.insertAnalysis(analysis);
    }

    @Override
    public Long countTweetAnalysis() {
        return twitterDao.countTweetAnalysis();
    }

    @Override
    public Map<String, Long> getAnalysisStatisticByKeyAndTime(List<String> keyWords, Date datePoint, Long preSeconds) {
        Date startDate = new Date(datePoint.getTime() - preSeconds*1000);
        List<Map<String, Object>> list = twitterDao.queryAnalysisStatisticCountByKeysAndTime(startDate, datePoint, keyWords);
        HashMap<String, Long> result = new HashMap<>();
        for (Map<String, Object> item : list){
            item.putIfAbsent("num", 0L);
            result.put((String) item.get("analysis"), (Long)(item.get("num")));
        }
        return  result;
    }

    /**
     * 重大bug：有人改名了，screen_name 是可以改的，奈奈滴，所以用户信息要更新呐！！
     *          不然的话，通过tweet查出来的用户信息就对不上了
     * */
    @Override
    public int insertTwitterUser(TwitterUserPO user) {
        user.setCatchTime(new Date());
        int result = -1;
        TwitterUserPO existUser = twitterDao.queryUserById(user.getId());
        if (existUser == null){
            try {
                result = twitterDao.insertTwitterUser(user);
            } catch (DuplicateKeyException e){
                log.error("twitter user重复主键值："+user.getId()+", "+user.getCatchTime().toString());
            }
        }
        else {
            if (!existUser.equals(user)) {
                result = twitterDao.updateUserById(user);
//                log.info("更新用户信息: \n"+ existUser.toString()+ "\n=====>>>\n" +user.toString());
            }
        }
        return result;
    }

    @Override
    public TwitterUserPO getUserById(BigInteger id) {
        return twitterDao.queryUserById(id);
    }
}
