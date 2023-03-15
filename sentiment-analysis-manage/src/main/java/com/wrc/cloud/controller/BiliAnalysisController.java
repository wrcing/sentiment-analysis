package com.wrc.cloud.controller;


import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.BiliReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/4 12:10
 */
@RestController
@RequestMapping("/api/bili/analysis")
@Slf4j
public class BiliAnalysisController {

    @Resource
    private BiliReplyService biliReplyService;

    @GetMapping
    public ResponseResult getVideoAnalysis(){
        return new ResponseResult("not finished");
    }

}
