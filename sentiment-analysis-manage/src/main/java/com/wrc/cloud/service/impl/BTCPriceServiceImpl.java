package com.wrc.cloud.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wrc.cloud.PO.CoinPrice;
import com.wrc.cloud.dao.BTCPriceDao;
import com.wrc.cloud.service.BTCPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/9 17:37
 */
@Service
@Slf4j
public class BTCPriceServiceImpl implements BTCPriceService {

    @Resource
    private BTCPriceDao btcPriceDao;

    @Override
    @CacheEvict(value = "BTCPriceService", condition = "btcPrice.isPredictedPrice(btcPrice)", allEntries = true)
    public int saveBTCPrice(CoinPrice btcPrice) {
        // 这里就直接插入把，实在是想不起来什么好方法判断相同，反正预测数据重新生成一遍也不是多耗时
        btcPrice.setCreatedAt(new Date());
        if (CoinPrice.isPredictedPrice(btcPrice) && btcPrice.getPredictTime() == null){
            return -2;
        }
        if (btcPriceDao.queryOnePriceByTimeAndType(btcPrice.getTimePoint(), btcPrice.getPriceType()) != null){
            // 库中已有 该时间点、该类型的 数据
            log.info("price 已有数据："
                    + DateUtil.format(btcPrice.getTimePoint(), "yyyy-MM-dd HH:mm:ss")
                    +", type: "+btcPrice.getPriceType());
            return -3;
        }
        return btcPriceDao.insert(btcPrice);
    }

    /**
     * 返回的没有null，null转化为空的CoinPrice
     * */
    @Override
    @Cacheable(cacheNames = "BTCPriceService:price",
            keyGenerator = "simpleObjAndListKeyGeneratorWithoutMethodName",
            unless="#result == null")
    public CoinPrice getOnePriceByTimeAndType(Date timePoint, Integer type) {
        // 查时间点之后（包括该点）的最近的数据
        CoinPrice price = btcPriceDao.queryOnePriceByTimeAndTypeWithLaterData(timePoint, type);
        if (price == null){
            // 往前10min的价格数据
            List<CoinPrice> prices = btcPriceDao.queryPricesByTimeAndType(new Date(timePoint.getTime() - 1000 * 60 * 10),
                    timePoint, type, null);
            if (!prices.isEmpty()){
                price = prices.get(0);
                // 取距离该时间点最近的
                for (CoinPrice p : prices){
                    if (price.getTimePoint().before(p.getTimePoint())) price = p;
                }
            }
            else {
                price = new CoinPrice();
            }
        }
        return price;
    }
    @Override
    @CachePut(cacheNames = "BTCPriceService:price",
            keyGenerator = "simpleObjAndListKeyGeneratorWithoutMethodName",
            unless="#result == null")
    public CoinPrice getOnePriceByTimeAndTypeWithCacheUpdate(Date timePoint, Integer type) {
        return getOnePriceByTimeAndType(timePoint, type);
    }

    @Override
    @Cacheable(cacheNames = "BTCPriceService", keyGenerator = "simpleObjAndListKeyGenerator")
    public List<CoinPrice> getPricesByTimeAndType(Date startTime, Date endTime, Integer priceType, Long sepSeconds) {
        if (startTime == null) return new LinkedList<>();
        if (endTime == null) endTime = new Date();
        if (startTime.after(endTime)) return new LinkedList<>();
        if (priceType == null) priceType = CoinPrice.BITFINEX_ACTUAL_PRICE_TPYE;
        return btcPriceDao.queryPricesByTimeAndType(startTime, endTime, priceType, sepSeconds);
    }
}
