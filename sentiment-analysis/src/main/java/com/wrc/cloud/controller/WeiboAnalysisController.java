package com.wrc.cloud.controller;


import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.AnalysisService;
import com.wrc.cloud.service.WeiboCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/3 17:59
 */
@RestController
@RequestMapping("/api/weibo/analysis")
@Slf4j
public class WeiboAnalysisController {

    @Resource
    private AnalysisService analysisService;

    /**
     * 返回list[评论分析结果]
     * */
    @GetMapping
    public ResponseResult<List<AnalysisPO>>
        getWeiboAnalysis(@RequestParam String url,
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
        return new ResponseResult(analysisService.getWeiboBlogAnalysis(condition));
    }



}
