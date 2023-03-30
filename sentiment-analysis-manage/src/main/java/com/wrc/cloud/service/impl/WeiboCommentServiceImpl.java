package com.wrc.cloud.service.impl;




import com.wrc.cloud.DTO.AnalysisDTO;
import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.WeiboCommentPO;
import com.wrc.cloud.dao.WeiboCommentDao;
import com.wrc.cloud.service.WeiboCommentService;
import com.wrc.cloud.util.WrcURLUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/29 15:35
 */
@Service
public class WeiboCommentServiceImpl implements WeiboCommentService {

    @Resource
    private WeiboCommentDao commentDao;


    @Autowired
    RabbitTemplate rabbitTemplate;



    @Override
    public WeiboCommentPO queryById(BigInteger id) {
        return commentDao.queryById(id);
    }

    @Override
    public int insert(WeiboCommentPO commentPO) {
        commentPO.setCatchTime(new Date());


        // 修正url，去除参数
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
        int result = 0;
        if (commentDao.queryById(commentPO.getId()) != null){
            // 数据库中已有记录，则更新记录
            result = commentDao.update(commentPO);
        }
        else {
            result = commentDao.insert(commentPO);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("siteId", AnalysisPO.WEIBO_SITE_ID);
        map.put("id", commentPO.getId());
        rabbitTemplate.convertAndSend("Cloud.AnalysisExchange", "AnalysisOrderRoute",
                map);
        return result;
    }

    @Override
    public Long getCount(WeiboCommentCondition condition) {
        return commentDao.count(condition);
    }


    /**
     * 耗时，要cache结果，5秒更新cache
     *
     * */
    @Override
    public List<AnalysisDTO> getWeiboBlogAnalysis(WeiboCommentCondition condition) {
        if (!condition.getUrl().startsWith("https://weibo.com/"))
            return new LinkedList<>();

        // 这里通过url查其所有的comment，取第一条comment的blogId
        WeiboCommentPO commentPO = commentDao.queryBlogIdByUrl(condition.getUrl());
        if (commentPO == null) {
            return new LinkedList<>();
        }
        condition.setBlogId(commentPO.getBlogId());

        // 清除url，避免搜索时用到
        // 因为url为抓取时访问的页面，有多种页面都能访问到一条微博的评论
        condition.setUrl(null);

        // 已处理的文本list
        List<AnalysisPO> analysisList = commentDao.queryAnalysisByCondition(condition);
        // 未处理的文本list，将交由Python进行处理
        List<WeiboCommentPO> unhandledCommentList = commentDao.queryBlogCommentWithoutAnalysis(condition.getBlogId());

        // 异步调用：有没处理的评论，发消息给mq，让Python抓紧处理了
        Map<String, String> commentInfo = new HashMap<>();
        for (WeiboCommentPO unhandledComment : unhandledCommentList){
            commentInfo.put("siteId", AnalysisPO.WEIBO_SITE_ID.toString());
            commentInfo.put("id", unhandledComment.getId().toString());
            rabbitTemplate.convertAndSend(
                    "Cloud.AnalysisExchange","AnalysisOrderRoute",
                    commentInfo
            );
        }

        // 不返回这些 还没有处理的评论
        // 为分析结果添加 元数据
        // 查询符合该blog下符合条件的评论
        List<WeiboCommentPO> comments = commentDao.queryByCondition(condition);
        LinkedList<AnalysisDTO> resultList = new LinkedList<>();
        for (AnalysisPO p : analysisList){
            AnalysisDTO analysisDTO = new AnalysisDTO();
            analysisDTO.setAnalysisPO(p);
            for (WeiboCommentPO c : comments) {
                if (c.getId().equals(p.getId())) {
                    analysisDTO.setLocation(c.getLocation());
                    break;
                }
            }
            resultList.add(analysisDTO);
        }
        return resultList;
    }


    @Override
    public Long getAnalysisCount(AnalysisPO analysisPO) {
        return commentDao.countAnalysis(analysisPO);
    }

    @Override
    public int saveAnalysis(AnalysisPO analysisPO) {
        int result = -1;
//        System.out.println(analysisPO);
        List<AnalysisPO> analysisPOs = commentDao.queryAnalysisById(analysisPO.getId());

        if (analysisPOs == null || analysisPOs.isEmpty()){
            result = commentDao.insertAnalysis(analysisPO);
        }
        else {
            result = commentDao.updateAnalysis(analysisPO);
        }
        return result;
    }

    @Override
    public List getStatisticInfo() {
        return commentDao.getStatisticInfo(AnalysisPO.WEIBO_SITE_ID);
    }
}
