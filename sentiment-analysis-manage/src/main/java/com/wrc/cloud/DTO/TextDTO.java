package com.wrc.cloud.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/5 13:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextDTO {

    /**
     * 网站id，0-bili，1-weibo，2-Twitter
     * */
    private Integer siteId;

    /**
     * 在各个网站中的id
     * */
    private BigInteger id;

    private String text;
}
