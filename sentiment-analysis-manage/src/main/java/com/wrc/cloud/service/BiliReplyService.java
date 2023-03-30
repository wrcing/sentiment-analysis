package com.wrc.cloud.service;

import com.wrc.cloud.DO.BiliReplyDO;
import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.BiliReplyPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigInteger;

/**
 * (TBiliReply)表服务接口
 *
 * @author makejava
 * @since 2023-01-20 09:23:40
 */
public interface BiliReplyService {

    /**
     * 通过ID查询单条数据
     *
     * @param rpid 主键
     * @return 实例对象
     */
    BiliReplyDO queryById(BigInteger rpid);

    /**
     * 分页查询
     *
     * @param tBiliReply  筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<BiliReplyDO> queryByPage(BiliReplyDO tBiliReply, PageRequest pageRequest);

    /**
     * 有条件地 查询数据量
     */
    Long getCount(BiliReplyPO replyPO);


    /**
     * 新增数据
     *
     * @param tBiliReply 实例对象
     * @return 实例对象
     */
    int insert(BiliReplyDO tBiliReply);

    /**
     * 修改数据
     *
     * @param tBiliReply 实例对象
     * @return 实例对象
     */
    int update(BiliReplyDO tBiliReply);

    /**
     * 通过主键删除数据
     *
     * @param rpid 主键
     * @return 是否成功
     */
    int deleteById(BigInteger rpid);


    public Long getAnalysisCount(AnalysisPO condition);
}
