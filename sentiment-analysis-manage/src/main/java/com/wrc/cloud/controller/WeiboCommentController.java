package com.wrc.cloud.controller;

import com.wrc.cloud.DTO.AnalysisDTO;
import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.WeiboCommentPO;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.WeiboCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wrc
 * @description: 负责 评论数据 与 情感数据 的管理
 * @date : 2023/1/29 13:39
 */
@RestController
@Slf4j
@RequestMapping("/api/weibo")
public class WeiboCommentController {

    @Resource
    private WeiboCommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<Integer> add(@RequestBody WeiboCommentPO commentPO) {
        // 去除定位“来自”
        if (commentPO.getLocation() != null) {
            commentPO.setLocation(commentPO.getLocation().replaceFirst("来自", ""));
        }
        int result = commentService.insert(commentPO);

        return ResponseEntity.ok(result);
    }

    /**
     * 返回已有数据量
     * */
    @GetMapping("/comment/count")
    public ResponseResult<Long> getCount(@RequestParam String url,
                                         @RequestParam String location,
                                         @RequestParam Integer minLike,
                                         @RequestParam Integer minReply,
                                         @RequestParam List<Long> timeRange){
        WeiboCommentCondition condition = new WeiboCommentCondition();
        condition.setUrl(url);
        condition.setLocation(location);
        condition.setMinLike(minLike);
        condition.setMinReply(minReply);
        condition.setTimeRange(timeRange);
//        log.info(condition.toString());
        return new ResponseResult<>(commentService.getCount(condition));
    }


    /**
     * 返回list[评论分析结果]
     * */
    @GetMapping("/analysis")
    public ResponseResult<List<AnalysisDTO>> getWeiboAnalysis(
            @RequestParam String url,
            @RequestParam String location,
            @RequestParam Integer minLike,
            @RequestParam Integer minReply,
            @RequestParam List<Long> timeRange) {
        WeiboCommentCondition condition = new WeiboCommentCondition();
        condition.setUrl(url);
        condition.setLocation(location);
        condition.setMinLike(minLike);
        condition.setMinReply(minReply);
        condition.setTimeRange(timeRange);
//        log.info(condition.toString());
        return ResponseResult.success(commentService.getWeiboBlogAnalysis(condition));
    }


    /**
     * 返回已情感分析处理 数据量
     * */
    @GetMapping("/analysis/count")
    public ResponseResult<Long> getAnalysisCount(){
        Long analysisCountWeibo = commentService.getAnalysisCount(new AnalysisPO(AnalysisPO.WEIBO_SITE_ID));
        return ResponseResult.success(analysisCountWeibo);
    }

    /**
     * 微博的整体统计信息
     *
     * 返回：各个情绪的评论数量
     * */
    @GetMapping("/analysis/statistic")
    public ResponseResult<List> getWeiboStatisticInfo(){
        List statisticInfo = commentService.getStatisticInfo();
        return ResponseResult.success(statisticInfo);
    }

}
