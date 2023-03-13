package com.wrc.cloud.service.impl;




import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.WeiboCommentPO;
import com.wrc.cloud.dao.AnalysisDao;
import com.wrc.cloud.dao.WeiboCommentDao;
import com.wrc.cloud.service.WeiboCommentService;
import com.wrc.cloud.util.WrcURLUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/29 15:35
 */
@Service
public class WeiboCommentServiceImpl implements WeiboCommentService {

    @Resource
    private WeiboCommentDao commentDao;

    @Override
    public WeiboCommentPO queryById(BigInteger id) {
        return commentDao.queryById(id);
    }

    @Override
    public int insert(WeiboCommentPO commentPO) {
        commentPO.setCatchTime(new Date());

        // 去除定位“来自”
        if (commentPO.getLocation() != null) {
            commentPO.setContentRaw(commentPO.getContentRaw().replaceFirst("来自", ""));
        }

        commentPO.setUrl(WrcURLUtil.getUrlWithoutParam(commentPO.getUrl()));
        // 采集来的二级评论的blog_id实际为其对应的一级评论id
        // 一级评论的 id 等于 root_id
        // 把 二级评论的blog_id 改为 真正的blog_id
        if (!commentPO.getId().equals(commentPO.getRootId())){
            // commentPO不是一级评论
            // 获取其对应的一级评论
            WeiboCommentPO comment1 = commentDao.queryById(commentPO.getRootId());
            commentPO.setBlogId(comment1.getBlogId());
        }

//        System.out.println(commentDao.queryById(commentPO.getId()).toString());
        if (commentDao.queryById(commentPO.getId()) != null){
            // 数据库中已有记录，则更新记录
            return commentDao.update(commentPO);
        }
        return commentDao.insert(commentPO);
    }

    @Override
    public Long getCount(WeiboCommentCondition condition) {
        long count = commentDao.count(condition);
        return count;
    }


}
