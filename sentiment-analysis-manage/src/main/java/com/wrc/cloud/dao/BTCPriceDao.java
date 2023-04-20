package com.wrc.cloud.dao;


import com.wrc.cloud.PO.CoinPrice;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * (CoinPrice)表数据库访问层
 *
 * @author wrc
 * @since 2023-04-09 17:01:39
 */
public interface BTCPriceDao {

    /**
     * 通过ID查询单条数据
     */
    CoinPrice queryById(BigInteger id);

    /**
     * 查询指定行数据
     *
     * @param btcPrice 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<CoinPrice> queryAllByLimit(CoinPrice btcPrice, @Param("pageable") Pageable pageable);

    List<CoinPrice> queryPricesByTimeAndType(@Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime,
                                             @Param("priceType") Integer priceType,
                                             @Param("sepSeconds") Long sepSeconds);

    CoinPrice queryOnePriceByTimeAndType(@Param("timePoint") Date timePoint,
                                         @Param("priceType") Integer priceType);

    /**
     * 返回该时间点数据，若没有，返回该时间点之后的最近的数据
     * */
    CoinPrice queryOnePriceByTimeAndTypeWithLaterData(@Param("timePoint") Date timePoint,
                                         @Param("priceType") Integer priceType);
    /**
     * 统计总行数
     */
    long count(CoinPrice btcPrice);

    /**
     * 新增数据
     *
     * @param btcPrice 实例对象，没有id
     * @return 影响行数
     */
    int insert(CoinPrice btcPrice);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CoinPrice> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CoinPrice> entities);


    /**
     * 修改数据
     *
     * @param btcPrice 实例对象
     * @return 影响行数
     */
    int update(CoinPrice btcPrice);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(BigInteger id);

}

