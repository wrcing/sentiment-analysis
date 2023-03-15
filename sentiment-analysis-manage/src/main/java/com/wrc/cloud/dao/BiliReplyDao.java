package com.wrc.cloud.dao;

import com.wrc.cloud.PO.BiliReplyPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;

/**
 * (TBiliReply)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-20 09:23:34
 */
public interface BiliReplyDao {

    /**
     * 通过ID查询单条数据
     *
     * @param rpid 主键
     * @return 实例对象
     */
    BiliReplyPO queryById(BigInteger rpid);

    /**
     * 查询指定行数据
     *
     * @param tBiliReply 查询条件
     * @param pageable   分页对象
     * @return 对象列表
     */
    List<BiliReplyPO> queryAllByLimit(BiliReplyPO tBiliReply, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tBiliReply 查询条件
     * @return 总行数
     */
    long count(BiliReplyPO tBiliReply);

    /**
     * 新增数据
     *
     * @param tBiliReply 实例对象
     * @return 影响行数
     */
    int insert(BiliReplyPO tBiliReply);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TBiliReply> 实例对象列表，请检查元素主键均不存在于数据库
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BiliReplyPO> entities);


    /**
     * 修改数据
     *
     * @param tBiliReply 实例对象
     * @return 影响行数
     */
    int update(BiliReplyPO tBiliReply);

    /**
     * 通过主键删除数据
     *
     * @param rpid 主键
     * @return 影响行数
     */
    int deleteById(BigInteger rpid);

}

