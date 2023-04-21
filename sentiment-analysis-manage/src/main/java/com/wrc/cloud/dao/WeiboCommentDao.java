package com.wrc.cloud.dao;

import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.WeiboCommentPO;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/29 13:58
 */
public interface WeiboCommentDao {

    public WeiboCommentPO queryById(BigInteger id);

    /**
     * 通过帖子id查评论
     * 这里只查询帖子的一级评论
     *
     * 其实帖子的一级评论也是帖子
     * */
    public List<WeiboCommentPO> queryByCondition(WeiboCommentCondition condition);

    public WeiboCommentPO queryBlogIdByUrl(String url);

    public Long count(WeiboCommentCondition condition);

    public int insert(WeiboCommentPO commentPO);

    public int update(WeiboCommentPO commentPO);

    public int deleteById(BigInteger id);

    /**
     * 通过 blogId 查询 评论的分析
     * 这个 siteId 不应该存在，但不想再给每个网站的评论分析建库了，就都放在了一个表里
     * 其实从一开时就可以把 分析结果 作为属性，直接放到评论表里
     * 但又要改之前的代码，。。。，这是分析与设计的失败！！！
     *
     * 查询符合条件的评论 的分析结果
     *
     * 那就定死 siteId 吧，0-bili， 1-weibo，  2-Twitter
     * */
    public List<AnalysisPO> queryAnalysisByCondition(WeiboCommentCondition condition);

    /**
     * 通过 blogId 查询 未分析过的评论
     *
     * 不需要条件
     * */
    public List<WeiboCommentPO> queryBlogCommentWithoutAnalysis(@Param("blogId") BigInteger blogId);


    /**
     * 以下为 analysis分析结果 管理
     * 通过siteid + id来查
     * */
    public List<AnalysisPO> queryAnalysisById(BigInteger id);

    public List<AnalysisPO> queryAnalysis(AnalysisPO analysisPO);

    public Long countAnalysis(AnalysisPO analysisPO);

    public int insertAnalysis(AnalysisPO analysisPO);

    public int updateAnalysis(AnalysisPO analysisPO);

    public int deleteAnalysisById(AnalysisPO analysisPO);

    public List getStatisticInfo(@Param("weiboSiteId") Integer weiboSiteId);
}
