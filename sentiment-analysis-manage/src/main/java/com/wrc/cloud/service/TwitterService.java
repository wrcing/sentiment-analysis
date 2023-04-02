package com.wrc.cloud.service;

import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.PO.TwitterUserPO;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/1 14:11
 */

public interface TwitterService {

    int insertTweet(TweetPO tweet);

    List<AnalysisPO> getAllAnalysisByUrl(String url);

    List<TweetPO> getTweetsByTimeAndKeyWords(Date startTime, Date endTime, List<String> keyWords);

    List<TweetPO> getConversationsByTime(Date startTime, Date endTime);

    Long countTweet();
    Long countTweetAnalysis();

    int insertTwitterUser(TwitterUserPO user);

    TwitterUserPO getUserById(BigInteger id);
}
