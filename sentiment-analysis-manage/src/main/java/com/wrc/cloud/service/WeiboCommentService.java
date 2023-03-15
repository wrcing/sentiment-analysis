package com.wrc.cloud.service;


import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.WeiboCommentPO;

import java.math.BigInteger;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/29 15:34
 */
public interface WeiboCommentService {

    public WeiboCommentPO queryById(BigInteger id);

    public int insert(WeiboCommentPO commentPO);

    public Long getCount(WeiboCommentCondition condition);

}
