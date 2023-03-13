package com.wrc.cloud.dao;

import com.wrc.cloud.PO.AnalysisPO;

import java.math.BigInteger;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/4 12:13
 */
public interface AnalysisDao {

    /**
     * 通过siteid + id来查
     * */
    public AnalysisPO queryById(AnalysisPO analysisPO);

    public long count(AnalysisPO analysisPO);

    public int insert(AnalysisPO analysisPO);

    public int update(AnalysisPO analysisPO);

    public int deleteById(AnalysisPO analysisPO);

}
