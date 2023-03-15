package com.wrc.cloud.dao;

import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.WeiboCommentPO;

import java.math.BigInteger;
import java.util.List;

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

    public long count(WeiboCommentCondition condition);

    public int insert(WeiboCommentPO commentPO);

    public int update(WeiboCommentPO commentPO);

    public int deleteById(BigInteger id);

}
