package com.wrc.cloud.PO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/29 13:48
 */

@NoArgsConstructor
@Data
public class WeiboCommentPO {

    private BigInteger id;

    private BigInteger userId;

    private Date createTime;

    private String location;

    private String contentRaw;

    private Integer likeCount;

    private Integer commentNum;

    private BigInteger rootId;

    private String url;

    /**
     * 主贴的id
     * */
    private BigInteger blogId;

    private BigInteger maxId;

    private Date catchTime;
}
