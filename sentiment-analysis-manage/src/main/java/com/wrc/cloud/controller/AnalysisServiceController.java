package com.wrc.cloud.controller;


import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.WeiboCommentPO;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.BiliReplyService;
import com.wrc.cloud.service.WeiboCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * @author : wrc
 * @description: 与Python服务对接，不对外提供服务，负责 分析 及 对分析结果的管理
 * @date : 2023/2/3 17:59
 */
@RestController
@RequestMapping("/api/analysis")
@Slf4j
public class AnalysisServiceController {

    @Resource
    private WeiboCommentService weiboCommentService;

    @Resource
    private BiliReplyService biliReplyService;


    /**
     * 给Python分析使用
     * 获取一条评论
     * */
    @GetMapping("/comment")
    public ResponseResult<String> getWeiboSingleAnalysis(
            @RequestParam("siteId") Integer siteId,
            @RequestParam("id")BigInteger id)
    {
        if (AnalysisPO.WEIBO_SITE_ID.equals(siteId)) {
            WeiboCommentPO commentPO = weiboCommentService.queryById(id);
            if (commentPO == null) {
                return ResponseResult.error("no such weibo comment");
            }
            return ResponseResult.success(commentPO.getContentRaw());
        }
        return ResponseResult.error("no such site");
    }

    /**
     * 给Python分析使用
     * 保存一条分析结果
     * */
    @PostMapping
    public ResponseResult<String> saveAnalysisResult(@RequestBody AnalysisPO analysisPO) {
//        switch (siteId)

        if (AnalysisPO.WEIBO_SITE_ID.equals(analysisPO.getSiteId())){
            int result = weiboCommentService.saveAnalysis(analysisPO);
            return  ResponseResult.success(String.valueOf(result));
        }

        return ResponseResult.error("failed");
    }


}
