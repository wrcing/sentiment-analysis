package com.wrc.cloud.DTO;

import com.wrc.cloud.PO.AnalysisPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/8 9:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDTO {
    AnalysisPO analysisPO;

    /**
     * meta
     * */

    private String location;


}
