package com.wrc.cloud.controller;

import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.WeiboCommentPO;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.WeiboCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
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
//        log.info(commentPO.toString());
        int result = commentService.insert(commentPO);
        return ResponseEntity.ok(result);
    }

    /**
     * 返回已有数据量
     * */
    @GetMapping("/count")
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

}
