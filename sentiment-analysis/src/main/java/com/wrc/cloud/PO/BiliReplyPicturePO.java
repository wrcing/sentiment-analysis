package com.wrc.cloud.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/20 17:52
 */
@Data
@NoArgsConstructor
public class BiliReplyPicturePO implements Serializable {
    private static final long serialVersionUID = 104762609825830664L;

    private BigInteger id;

    private String url;

    private BigInteger replyId;

    /**
     * 图片大小
     */
    private Float size;
}
