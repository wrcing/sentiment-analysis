package com.wrc.cloud.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/3/31 16:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TweetPO {

    private BigInteger id;

    private String source;

    private BigInteger userId;

    private BigInteger conversationId;

    private String fullText;

    private Date createdAt;

    private Date catchTime;

    private Integer bookmarkCount;

    private Integer favoriteCount;

    private String lang;

    private Integer quoteCount;

    private Integer replyCount;

    private Integer retweetCount;

    private Integer viewCount;

    private String url;

}
