package com.wrc.cloud.service;


import com.wrc.cloud.DTO.AnalysisDTO;
import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.WeiboCommentPO;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/29 15:34
 */
public interface WeiboCommentService {

    public WeiboCommentPO queryById(BigInteger id);

    public int insert(WeiboCommentPO commentPO);

    public Long getCount(WeiboCommentCondition condition);

    /**
     * 以下为分析 管理控制
     * */

    public List<AnalysisDTO> getWeiboBlogAnalysis(WeiboCommentCondition condition);

    public Long getAnalysisCount(AnalysisPO analysisPO);

    public int saveAnalysis(AnalysisPO analysisPO);

    public List getStatisticInfo();
}
