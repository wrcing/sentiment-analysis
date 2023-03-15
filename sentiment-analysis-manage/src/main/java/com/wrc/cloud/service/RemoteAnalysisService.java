package com.wrc.cloud.service;

import com.wrc.cloud.DTO.TextDTO;
import com.wrc.cloud.PO.AnalysisPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/5 13:15
 */
@Component
@FeignClient(name = "ANALYSIS-SERVICE")
public interface RemoteAnalysisService {

    @PostMapping("/analysis")
    public List<AnalysisPO> getAnalysisList(List<TextDTO> list);
}
