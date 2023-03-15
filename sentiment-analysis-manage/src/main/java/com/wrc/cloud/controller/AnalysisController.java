package com.wrc.cloud.controller;


import com.wrc.cloud.entities.ResponseResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

/**
 * @author : wrc
 * @description: 提供Python分析所需的评论数据，并插入Python传来的分析数据
 * @date : 2023/2/21 11:37
 */

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    /**
     * siteId.id
     * */
    @GetMapping("/text")
    public ResponseResult getTextByMultiplyId(@RequestParam("siteId") Integer siteId,
                                              @RequestParam("id") BigInteger id) {

        return ResponseResult.success("");
    }


    @PostMapping
    public ResponseResult saveAnalysis(@RequestBody Map<String, String> map){

        return ResponseResult.success("");
    }
}
