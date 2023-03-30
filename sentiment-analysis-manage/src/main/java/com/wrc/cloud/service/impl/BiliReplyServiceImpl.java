package com.wrc.cloud.service.impl;

import com.wrc.cloud.DO.BiliReplyDO;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.BiliReplyPO;
import com.wrc.cloud.PO.BiliReplyPicturePO;
import com.wrc.cloud.dao.BiliReplyDao;
import com.wrc.cloud.dao.BiliReplyPictureDao;
import com.wrc.cloud.service.BiliReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * (TBiliReply)表服务实现类
 *
 * @author makejava
 * @since 2023-01-20 09:23:41
 */
@Service("tBiliReplyService")
@Slf4j
public class BiliReplyServiceImpl implements BiliReplyService {
    @Resource
    private BiliReplyDao biliReplyDao;

    @Resource
    private BiliReplyPictureDao pictureDao;

    /**
     * 通过ID查询单条数据
     *
     * @param rpid 主键
     * @return 实例对象
     */
    @Override
    public BiliReplyDO queryById(BigInteger rpid) {
        BiliReplyDO replyDO = new BiliReplyDO();
        replyDO.setReply(this.biliReplyDao.queryById(rpid));

        BiliReplyPicturePO pictureSelect = new BiliReplyPicturePO();
        pictureSelect.setReplyId(rpid);
        replyDO.setPictures(this.pictureDao.queryAllByLimit(pictureSelect));

        return replyDO;
    }

    /**
     * 分页查询
     *
     * @param replyDOSelect  筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<BiliReplyDO> queryByPage(BiliReplyDO replyDOSelect, PageRequest pageRequest) {
        long total = this.biliReplyDao.count(replyDOSelect.getReply());
        List<BiliReplyPO> replies = this.biliReplyDao.queryAllByLimit(replyDOSelect.getReply(), pageRequest);
        List<BiliReplyDO> repliesDO = new LinkedList<>();
        for (BiliReplyPO replyPO : replies){
            BiliReplyDO replyDO = new BiliReplyDO();
            replyDO.setReply(replyPO);
            replyDO.setPictures(this.pictureDao.queryByReplyId(replyPO.getRpid()));
            repliesDO.add(replyDO);
        }
        return new PageImpl<>(repliesDO, pageRequest, total);
    }

    /**
     * 查询数据量，行数
     *
     * @param replyPO  筛选条件
     * @return 查询结果
     */
    @Override
    public Long getCount(BiliReplyPO replyPO) {
        return biliReplyDao.count(replyPO);
    }

    /**
     * 新增数据
     *
     * @param replyDO 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(BiliReplyDO replyDO) {
        BiliReplyPO reply = replyDO.getReply();
        reply.setCatchTime(new Date());
        if(reply.getLocation() != null) {
            reply.setLocation(reply.getLocation().replace("IP属地：", ""));
        }


//        log.info(replyDO.toString());
        int result;
        if (biliReplyDao.queryById(replyDO.getReply().getRpid()) != null){
            // 已经有该reply了
            result = this.update(replyDO);
        }
        else {
            // 数据库中没有该记录
            result = this.biliReplyDao.insert(replyDO.getReply());
            for (BiliReplyPicturePO picturePO : replyDO.getPictures()){
                if (this.pictureDao.queryById(picturePO.getId()) == null) {
                    result = result == 0 ? 0 : this.pictureDao.insert(picturePO);
                }
                else {
                    result = result == 0 ? 0 : this.pictureDao.update(picturePO);
                }
            }
        }
        return result;
    }

    /**
     * 修改数据
     *
     * @param biliReplyDO 实例对象
     * @return 实例对象
     */
    @Override
    public int update(BiliReplyDO biliReplyDO) {
        biliReplyDO.getReply().setCatchTime(new Date());
        int result = this.biliReplyDao.update(biliReplyDO.getReply());
        for (BiliReplyPicturePO picturePO : biliReplyDO.getPictures()){
            if (this.pictureDao.queryById(picturePO.getId()) != null){
                result = result == 0 ? 0: this.pictureDao.update(picturePO);
            }
            else {
                result = result == 0 ? 0: this.pictureDao.insert(picturePO);
            }
        }
        return result;
    }

    /**
     * 通过主键删除数据
     *
     * @param rpid 主键
     * @return 是否成功
     */
    @Override
    public int deleteById(BigInteger rpid) {
        return this.biliReplyDao.deleteById(rpid);
    }


    @Override
    public Long getAnalysisCount(AnalysisPO condition) {
        condition.setSiteId(AnalysisPO.BILI_SITE_ID);
        return biliReplyDao.countAnalysis(condition);
    }
}
