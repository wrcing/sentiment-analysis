package com.wrc.cloud.DO;

import com.wrc.cloud.DTO.BiliReplyDTO;
import com.wrc.cloud.PO.BiliReplyPO;
import com.wrc.cloud.PO.BiliReplyPicturePO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : wrc
 * @description: Domain Object--bilibili reply
 * @date : 2023/1/20 17:55
 */
@Data
@NoArgsConstructor
public class BiliReplyDO implements Serializable {

    private BiliReplyPO reply;

    private List<BiliReplyPicturePO> pictures;

    public BiliReplyDO(BiliReplyDTO replyDTO){
        this.reply = new BiliReplyPO();
        this.reply.setRpid(replyDTO.getRpid());
        this.reply.setMid(replyDTO.getMid());
        this.reply.setUrl(replyDTO.getUrl());
        this.reply.setOid(replyDTO.getOid());
        this.reply.setCtime(replyDTO.getCtime());
        this.reply.setRoot(replyDTO.getRoot());
        this.reply.setParent(replyDTO.getParent());
        this.reply.setLocation(replyDTO.getLocation());
        this.reply.setMessage(replyDTO.getMessage());
        this.reply.setLike(replyDTO.getLike());
        this.reply.setUpAction(replyDTO.getUpAction());
        this.reply.setType(replyDTO.getType());
        this.reply.setStat(replyDTO.getStat());
        this.reply.setReplyNum(replyDTO.getReplyNum());

        this.pictures = new LinkedList<>();
        if (replyDTO.getPictures() != null){
            for (String url : replyDTO.getPictures()){
                BiliReplyPicturePO picturePO = new BiliReplyPicturePO();
                picturePO.setReplyId(replyDTO.getRpid());
                picturePO.setUrl(url);
                this.pictures.add(picturePO);
            }
        }
    }
}
