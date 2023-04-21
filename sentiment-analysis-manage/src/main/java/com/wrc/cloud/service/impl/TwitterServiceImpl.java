package com.wrc.cloud.service.impl;

import com.google.common.collect.Lists;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.PO.TwitterUserPO;
import com.wrc.cloud.dao.TwitterDao;
import com.wrc.cloud.service.TwitterService;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String TWITTER_USER_PREFIX = "twitter:user:";

    private static List<String> SENTIMENTS = Lists.newArrayList("surprise", "like", "happy", "sad",
            "neutral", "angry", "disgust", "fear");

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
    public List<TweetPO> getTweetsByTimeAndTexts(Date startTime, Date endTime, List<String> texts) {
        if (!startTime.before(endTime)){
            return new LinkedList<>();
        }
        //  若要查询某时间段所有的tweet，传一个[""]即可
        if(texts == null || texts.isEmpty()){
            return new LinkedList<>();
        }
        // 转化为小写
        for (int i = 0; i < texts.size(); i++){
            texts.set(i, texts.get(i).toLowerCase());
        }
        return twitterDao.queryTweetsByTextAndTime(startTime, endTime, texts);
    }

    @Override
    public int saveConversationTweetKeyById(String key, BigInteger id) {
        return twitterDao.saveConversationTweetKeyById(key, id);
    }

    @Override
    public long countKeyWithConversationTweetId(String key, BigInteger id) {
        return twitterDao.countKeyWithTweetId(key, id);
    }

    @Override
    public int updateTweetKeyConversationTimeSlot() {
        return twitterDao.updateTweetKeyConversationTimeSlot();
    }

    @Override
    public List<TweetPO> getConversationsByTime(Date startTime, Date endTime) {
        return twitterDao.queryConversationIdsByTime(startTime, endTime);
    }

    @Override
    public List<TweetPO> getTweetsWithoutAnalysis(Date startTime, Date endTime) {
        if (startTime == null || startTime.after(endTime)) return new LinkedList<>();
        return twitterDao.queryTweetsWithoutAnalysis(startTime, endTime);
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
    @Cacheable(cacheNames = "TwitterService:AnalysisStatistic",
            keyGenerator = "simpleObjAndListKeyGeneratorWithoutMethodName",
            condition = "#preSeconds.compareTo(3600*24)>=0")
    public Map<String, Long> getAnalysisStatisticByKeyAndTime(List<String> keyWords, Date datePoint, Long preSeconds) {
        // 处理查询条件
        for (int i = 0; i < keyWords.size(); i++){
            keyWords.set(i, keyWords.get(i).toLowerCase());
        }
        Date startDate = new Date(datePoint.getTime() - preSeconds*1000);
        // 开始查询并处理结果
        List<Map<String, Object>> list = twitterDao.queryAnalysisStatisticCountByKeysAndTime(startDate, datePoint, keyWords);
        HashMap<String, Long> result = new HashMap<>();
        for (Map<String, Object> item : list){
            item.putIfAbsent("num", 0L);
            result.put((String) item.get("analysis"), (Long)(item.get("num")));
        }
        for (String sent : SENTIMENTS){
            if (!result.containsKey(sent)){
                result.put(sent, 0L);
            }
        }
        return  result;
    }

    @CachePut(cacheNames = "TwitterService:AnalysisStatistic",
            keyGenerator = "simpleObjAndListKeyGeneratorWithoutMethodName",
            condition = "#preSeconds.compareTo(3600*24)>=0")
    public Map<String, Long> updateCacheOfGetAnalysisStatisticByKeyAndTime(List<String> keyWords, Date datePoint, Long preSeconds){
        return getAnalysisStatisticByKeyAndTime(keyWords, datePoint, preSeconds);
    }

    /**
     * 重大bug：有人改名了，screen_name 是可以改的，奈奈滴，所以用户信息要更新呐！！
     *          不然的话，通过tweet查出来的用户信息就对不上了
     * */
    @Override
    public int insertTwitterUser(TwitterUserPO user) {
        if (user == null || user.getId() == null) return -1;
        if (Objects.equals(Boolean.FALSE,
                stringRedisTemplate.opsForValue().setIfAbsent(
                        TWITTER_USER_PREFIX + user.getId().toString(),
                        "1",
                        Duration.ofMinutes(10)))){
            // 已有id在缓存
            return 0;
        }

        user.setCatchTime(new Date());
        int result = -1;
        TwitterUserPO existUser = twitterDao.queryUserById(user.getId());
        if (existUser == null) {
            // 数据库与缓存均无
            try {
                result = twitterDao.insertTwitterUser(user);
            } catch (DuplicateKeyException e) {
                log.error("twitter user重复主键值：" + user.getId() + ", " + user.getCatchTime().toString());
            }
        }
        else if (!existUser.equals(user)) {
            result = twitterDao.updateUserById(user);
        }
        return result;
    }

    @Override
    public TwitterUserPO getUserById(BigInteger id) {
        return twitterDao.queryUserById(id);
    }
}
