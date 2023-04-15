package com.wrc.cloud.service;

import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.PO.TwitterUserPO;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/1 14:11
 */

public interface TwitterService {

    /**
     * tweet
     * */
    int insertTweet(TweetPO tweet);

    TweetPO queryTweetById(BigInteger id);

    List<TweetPO> getTweetsByTimeAndTexts(Date startTime, Date endTime, List<String> texts);

    int saveConversationTweetKeyById(String key, BigInteger id);

    long countKeyWithConversationTweetId(String key, BigInteger id);

    //爬虫去重用的
    List<TweetPO> getConversationsByTime(Date startTime, Date endTime);

    // 获取没有分析过的tweet, Time是tweet的创建时间
    List<TweetPO> getTweetsWithoutAnalysis(Date startTime, Date endTime);

    Long countTweet();

    /**
     * analysis
     * */
    int insertAnalysis(AnalysisPO analysis);

    List<AnalysisPO> getAllAnalysisByUrl(String url);

    Long countTweetAnalysis();

    // 价格预测所需的情绪统计数据，关键词、时间点、时间点以前的时间段
    // 返回每种情绪的数量
    // keys 会转化小写
    Map<String, Long> getAnalysisStatisticByKeyAndTime(List<String> keyWords, Date datePoint, Long preSeconds);

    /**
     * user
     * */
    int insertTwitterUser(TwitterUserPO user);

    TwitterUserPO getUserById(BigInteger id);
}
