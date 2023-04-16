package com.wrc.cloud.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author : wrc
 * @description: analysis已经不配再称为PO了，由于设计问题，现在决定将analysis直接并入各个网站的评论表中
 * @date : 2023/2/3 19:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisPO implements Cloneable {
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

    private Date createdAt;



    public AnalysisPO(Integer siteId){
        this.siteId = siteId;
    }

    @Override
    public AnalysisPO clone() throws CloneNotSupportedException {
        AnalysisPO analysisPO = (AnalysisPO) super.clone();
        // 后续也不会直接改变 siteId对象里的值，应该会直接赋值新对象
        // 更何况这还在 -127 - 128 之间
        analysisPO.setSiteId(this.siteId);
        analysisPO.setId(new BigInteger(String.valueOf(this.id)));
        analysisPO.setAnalysis(this.analysis);
        return analysisPO;
    }
}
