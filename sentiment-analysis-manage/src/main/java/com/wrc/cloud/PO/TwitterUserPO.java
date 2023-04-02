package com.wrc.cloud.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class TwitterUserPO {

    private BigInteger id;

    private String name;

    private String screenName;

    private String idStr;

    private Date createdAt;

    private String description;

    private String location;

    private Integer followersCount;

    private Date catchTime;
}
