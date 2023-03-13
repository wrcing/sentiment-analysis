package com.wrc.cloud.service;

import com.wrc.cloud.DTO.AnalysisDTO;
import com.wrc.cloud.DTO.WeiboCommentCondition;
import com.wrc.cloud.PO.AnalysisPO;

import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/5 9:55
 */
public interface AnalysisService {

    public List<AnalysisDTO> getWeiboBlogAnalysis(WeiboCommentCondition condition);

    public Long getCount(AnalysisPO analysisPO);


}
