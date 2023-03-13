package com.wrc.cloud.controller;

import com.wrc.cloud.dao.WeiboCommentDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/5 14:50
 */
@RestController("/api")
@Slf4j
public class ClearDatabaseController {

    @Resource
    private WeiboCommentDao weiboCommentDao;

}
