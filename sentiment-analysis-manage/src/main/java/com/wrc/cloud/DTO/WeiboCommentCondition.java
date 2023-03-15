package com.wrc.cloud.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/4 12:44
 */
@Data
@NoArgsConstructor
public class WeiboCommentCondition {

    private BigInteger blogId;

    /**
     * url 不含有 ?参数 或 #路径
     * 查找时是不带此条件的
     * */
    private String url;

    private String location;

    private Integer minLike;

    private Integer minReply;

    private Date startTime;

    private Date endTime;

    public void setUrl(String url){
        if (url == null){
            this.url = null;
            return;
        }
        url = url.split("\\?")[0];
        this.url = url.split("#")[0];
    }

    public void setTimeRange(List<Long> timeRange){
        if (timeRange == null || timeRange.size() == 0 || timeRange.size() > 2) return;
        if (timeRange.size() == 1){
            this.startTime = new Date(timeRange.get(0));
            this.endTime = new Date();
            return;
        }
        // 均为null，未指定时间范围
        if (timeRange.get(0) == null && timeRange.get(1) == null){
            return;
        }
        // 均不为空，即正常情况
        if (timeRange.get(0) != null && timeRange.get(1) != null){
            if (timeRange.get(0) <= timeRange.get(1)){
                this.startTime = new Date(timeRange.get(0));
                this.endTime = new Date(timeRange.get(1));
            }
            else {
                this.startTime = new Date(timeRange.get(1));
                this.endTime = new Date(timeRange.get(0));
            }
        }
        // 现在一个为空一个不为空，即只有一个时间点
        if (timeRange.get(0) != null ) {
            this.startTime = new Date(timeRange.get(0));
        }
        else {
            this.startTime = new Date(timeRange.get(1));
        }
    }
}
