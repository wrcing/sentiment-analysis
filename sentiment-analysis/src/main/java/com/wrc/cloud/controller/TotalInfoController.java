package com.wrc.cloud.controller;

import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.BiliReplyPO;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.AnalysisService;
import com.wrc.cloud.service.BiliReplyService;
import com.wrc.cloud.service.WeiboCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/4 23:41
 */
@RestController
@RequestMapping("/api/total")
@Slf4j
public class TotalInfoController {

    @Resource
    private WeiboCommentService weiboCommentService;

    @Resource
    private BiliReplyService biliReplyService;

    @Resource
    private AnalysisService analysisService;

    @GetMapping("/count")
    public ResponseResult getTotalCount(){
        // 总数
        Long weiboCount = weiboCommentService.getCount(new WeiboCommentCondition());
        Long biliCount = biliReplyService.getCount(new BiliReplyPO());
        Long twitterCount = 1L;
        Long totalNum = weiboCount + biliCount + twitterCount;
        // 已处理数量
        Long analysisCountWeibo = analysisService.getCount(new AnalysisPO(AnalysisPO.WEIBO_SITE_ID));
        Long analysisCountBili = analysisService.getCount(new AnalysisPO(AnalysisPO.BILI_SITE_ID));
        Long analysisCountTwitter = 1L;
        Long totalHandled = analysisCountBili + analysisCountWeibo + analysisCountTwitter;
        // 返回数据
        HashMap<String, Long> result = new HashMap<>();
        result.put("handled", totalHandled);
        result.put("unhandled", totalNum - totalHandled);
        return new ResponseResult<>(result);
    }
}
