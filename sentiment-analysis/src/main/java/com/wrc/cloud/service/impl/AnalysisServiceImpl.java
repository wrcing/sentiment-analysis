package com.wrc.cloud.service.impl;

import com.wrc.cloud.DTO.AnalysisDTO;
import com.wrc.cloud.DTO.TextDTO;
import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.WeiboCommentPO;
import com.wrc.cloud.dao.AnalysisDao;
import com.wrc.cloud.dao.WeiboCommentDao;
import com.wrc.cloud.service.AnalysisService;
import com.wrc.cloud.service.RemoteAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/5 9:59
 */
@Service
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {

    @Resource
    private AnalysisDao analysisDao;

    @Resource
    private WeiboCommentDao commentDao;

    @Resource
    private RemoteAnalysisService remoteAnalysisService;


    /**
     * 耗时，要cache结果，5秒更新cache
     * */
    @Override
    public List<AnalysisDTO> getWeiboBlogAnalysis(WeiboCommentCondition condition) {
        if (!condition.getUrl().startsWith("https://weibo.com/"))
            return new LinkedList<>();

        WeiboCommentPO commentPO = commentDao.queryBlogIdByUrl(condition.getUrl());
        if (commentPO == null) {
            return new LinkedList<>();
        }
        BigInteger blogId = commentPO.getBlogId();
        condition.setBlogId(blogId);
        // 清除url，避免搜索时用到
        // 因为url为抓取时访问的页面，有多种页面都能访问到一条微博的评论
        condition.setUrl(null);

        // 查询符合该blog下符合条件的评论
        List<WeiboCommentPO> comments = commentDao.queryByCondition(condition);
        // 已处理的文本list
        LinkedList<AnalysisPO> analysisList = new LinkedList<>();
        // 未处理的文本list，将交由Python进行处理
        LinkedList<TextDTO> unhandledTextList = new LinkedList<>();

        // 查询 已处理的analysis 所使用的条件
        AnalysisPO analysisPOCondition = new AnalysisPO();
        // 微博站点的 文本分析
        analysisPOCondition.setSiteId(AnalysisPO.WEIBO_SITE_ID);
        //数据库中通过评论id查询 每条微博评论的 处理结果
        for (WeiboCommentPO comment : comments){
            // 将微博评论id赋值到 查询条件
            analysisPOCondition.setId(comment.getId());
            AnalysisPO analysis = analysisDao.queryById(analysisPOCondition);
            // 没有结果 则 将text放入待处理list
            if (analysis == null){
//                analysisList.add(new AnalysisPO(AnalysisPO.WEIBO_SITE_ID, comment.getId(), "happy1"));
                unhandledTextList.add(new TextDTO(AnalysisPO.WEIBO_SITE_ID, comment.getId(), comment.getContentRaw()));
            }
            else {
                analysisList.add(analysis);
            }
        }
        // 使用feign，远程调用Python进行分析
        // 异步调用
        try {
            List<AnalysisPO> newAnalysisList = remoteAnalysisService.getAnalysisList(unhandledTextList);
            // 将刚处理所得的 分析数据 插入数据库
            for (AnalysisPO a : newAnalysisList){
                analysisDao.insert(a);
            }

            // 将 刚处理完的结果 合并入 已有的分析list
            analysisList.addAll(newAnalysisList);
        } catch (Exception e){
//            e.printStackTrace();
            for (TextDTO t : unhandledTextList){
                AnalysisPO analysisPO = new AnalysisPO();
                analysisPO.setSiteId(t.getSiteId());
                analysisPO.setId(t.getId());
                // 标记为 未知
                analysisPO.setAnalysis("0");
                analysisList.add(analysisPO);
            }
        }
        // 为分析结果添加 元数据
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
    public Long getCount(AnalysisPO analysisPO) {
        return analysisDao.count(analysisPO);
    }

}
