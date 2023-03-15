package com.wrc.cloud.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/3 19:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisPO {
    public static final Integer BILI_SITE_ID = 0;
    public static final Integer WEIBO_SITE_ID = 1;
    public static final Integer TWITTER_SITE_ID = 2;

    /**
     * 网站id，0-bili，1-weibo，2-Twitter
     * */
    private Integer siteId;

    /**
     * 在各个网站中的id
     * */
    private BigInteger id;

    private String analysis;

    public AnalysisPO(Integer siteId){
        this.siteId = siteId;
    }
}
