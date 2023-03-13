package com.wrc.cloud.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NegativeOrZero;
import java.math.BigInteger;
import java.util.Date;
import java.io.Serializable;

/**
 * (TUser)实体类
 *
 * @author makejava
 * @since 2023-02-06 21:15:21
 */
@Data
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -15423566427845778L;
    /**
     * 用户唯一标识
     */
    private BigInteger uid;

    private String username;

    private String password;

    private Integer gender;
    /**
     * 头像
     */
    private String photo;

    private String emai;

    private Date birthday;

    private Integer phoneNum;
    /**
     * 个人简介，150字上限
     */
    private String profile;
    /**
     * 账号状态，0正常，1冻结
     */
    private Integer stat;

    private Date createTime;

    private Date updateTime;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * qq号
     */
    private String qq;
    /**
     * 微信号
     */
    private String wechat;


    public BigInteger getUid() {
        return uid;
    }

    public void setUid(BigInteger uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmai() {
        return emai;
    }

    public void setEmai(String emai) {
        this.emai = emai;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Integer phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

}

