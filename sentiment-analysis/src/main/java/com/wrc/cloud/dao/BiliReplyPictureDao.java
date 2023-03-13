package com.wrc.cloud.dao;

import com.wrc.cloud.PO.BiliReplyPicturePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/20 18:15
 */
public interface BiliReplyPictureDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BiliReplyPicturePO queryById(BigInteger id);


    List<BiliReplyPicturePO> queryByReplyId(BigInteger replyId);

    /**
     * 查询指定行数据
     *
     * @param tBiliReply 查询条件
     * @param pageable   分页对象
     * @return 对象列表
     */
//    List<BiliReplyPicturePO> queryAllByLimit(BiliReplyPicturePO picturePO, @Param("pageable") Pageable pageable);
    List<BiliReplyPicturePO> queryAllByLimit(BiliReplyPicturePO picturePO);

    /**
     * 统计总行数
     *
     * @param tBiliReply 查询条件
     * @return 总行数
     */
    long count(BiliReplyPicturePO picturePO);

    /**
     * 新增数据
     *
     * @param tBiliReply 实例对象
     * @return 影响行数
     */
    int insert(BiliReplyPicturePO picturePO);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<BiliReplyPicturePO> 实例对象列表，请检查元素主键均不存在于数据库
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BiliReplyPicturePO> entities);


    /**
     * 修改数据
     *
     * @param tBiliReply 实例对象
     * @return 影响行数
     */
    int update(BiliReplyPicturePO picturePO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(BigInteger id);

}
