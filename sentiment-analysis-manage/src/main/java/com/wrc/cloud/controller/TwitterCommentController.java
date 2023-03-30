package com.wrc.cloud.controller;

import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.WeiboCommentPO;
import com.wrc.cloud.entities.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/3/29 16:53
 */

@Slf4j
@RestController
@RequestMapping("/api/twitter")
public class TwitterCommentController {

//    @PostMapping("/comment")
//    public ResponseEntity<Integer> add(@RequestBody WeiboCommentPO commentPO) {
//        int result = commentService.insert(commentPO);
//        return ResponseEntity.ok(result);
//    }

    /**
     * 返回已有数据量
     * */
    @GetMapping("/comment/count")
    public ResponseResult<Long> getCount(){

        return ResponseResult.success(6L);
    }
}

