package com.wrc.cloud.PO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/20 17:52
 */
@Data
public class BiliReplyPO implements Serializable {
    private static final long serialVersionUID = 103762609825830664L;
    /**
     * 评论id，据说有可能发生变化
     */
    private BigInteger rpid;
    /**
     * B站用户id
     */
    private BigInteger mid;
    /**
     * 视频的地址
     */
    private String url;
    /**
     * 视频的id，并不是BV*
     */
    private BigInteger oid;
    /**
     * 评论发布时间
     */

    private Date ctime;
    /**
     * 根评论id，顶层评论为0
     */
    private BigInteger root;
    /**
     * 回复的评论id
     */
    private BigInteger parent;

    private String location;

    private String message;

    /**
     * 点赞数
     */
    private Integer like;
    /**
     * 点赞1 或 回复2
     */
    private Integer upAction;
    /**
     * state，也不知道是啥
     */
    private Integer stat;

    private Integer type;

    private Date catchTime;

    private Integer replyNum;

}
