package com.wrc.cloud.dao;

import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.PO.TwitterUserPO;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/3/31 16:57
 */
public interface TwitterDao {

    /**
     * tweet的操作
     *
     * */
    int insertTweet(TweetPO tweet);

    /**
     * 通过 id 查一条 tweet
     * */
    TweetPO queryTweetById(BigInteger id);

    /**
     * 通过 url 查询 主体贴
     * */
    TweetPO queryTweetByUrl(String url);

    /**
     * 查询 主题帖下 所有分析结果
     *
     * */
    List<AnalysisPO> queryAnalysisByMainTweetId(BigInteger id);

    /**
     * 通过 text内容&&时间 查询 tweet
     *
     * 请务必检查 start < end
     * 返回所有能匹配 texts 中词的
     * */
    List<TweetPO> queryTweetsByTextAndTime(@Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime,
                                           @Param("texts") List<String> texts);

    int saveConversationTweetKeyById(@Param("key") String key,
                                     @Param("tweetId") BigInteger tweetId);

    long countKeyWithTweetId(@Param("key") String key,
                             @Param("tweetId") BigInteger tweetId);

    int updateTweetKeyConversationTimeSlot();

    /**
     * 查询已有的 conversation id
     * 最近一段时间已经爬过的就不在爬了
     * */
    List<TweetPO> queryConversationIdsByTime(@Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime);


    List<TweetPO> queryTweetsWithoutAnalysis(@Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime);

    /**
     * 统计数量
     * */
    Long countTweet();
    Long countTweetAnalysis();


    /**
     * 保存Analysis
     *
     * 不得不说这里太sb了，
     * 为甚么要新建一个库呢？直接把analysis存到tweet的库里，速度嘎嘎的
     * */
    int insertAnalysis(AnalysisPO analysis);

    AnalysisPO queryAnalysisById(BigInteger id);

    int updateAnalysis(AnalysisPO analysis);

    /**
     * 每一种情绪的计数
     * [
     * {sentiment: happy, num: num1},
     * {sentiment:   sad, num: num2}
     * ]
     * */
    List<Map<String, Object>> queryAnalysisStatisticCountByTextsAndTime(@Param("startTime") Date startTime,
                                                                       @Param("endTime") Date endTime,
                                                                       @Param("texts") List<String> texts);

    List<Map<String, Object>> queryAnalysisStatisticCountByKeysAndTime(@Param("startTime") Date startTime,
                                                                       @Param("endTime") Date endTime,
                                                                       @Param("keys") List<String> keys);


    /**
     * 以下为 twitter 用户的操作
     * */
    int insertTwitterUser(TwitterUserPO user);

    TwitterUserPO queryUserById(BigInteger id);

    int updateUserById(TwitterUserPO user);
}
