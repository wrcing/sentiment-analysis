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

    public boolean equals(TwitterUserPO user){
        // 没判断 null，埋个雷
        if(this == user){
            return true;
        }
        if (!this.id.equals(user.getId())){
            return false;
        }
        if (!this.name.equals(user.getName())){
            return false;
        }
        if (!this.screenName.equals(user.getScreenName())){
            return false;
        }
        if (!this.idStr.equals(user.getIdStr())){
            return false;
        }
        if (!this.createdAt.equals(user.getCreatedAt())){
            return false;
        }
        if (!this.description.equals(user.getDescription())){
            return false;
        }
        if (!this.location.equals(user.getLocation())){
            return false;
        }
        if (!this.followersCount.equals(user.getFollowersCount())){
            return false;
        }
        // catchTime 不作为用户信息，只是记录我们抓取它的时间
        return true;
    }
}
