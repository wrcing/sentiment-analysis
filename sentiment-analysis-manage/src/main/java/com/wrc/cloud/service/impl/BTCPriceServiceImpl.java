package com.wrc.cloud.service.impl;

import com.wrc.cloud.PO.CoinPrice;
import com.wrc.cloud.dao.BTCPriceDao;
import com.wrc.cloud.service.BTCPriceService;
import lombok.extern.slf4j.Slf4j;
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
    public int saveBTCPrice(CoinPrice btcPrice) {
        // 这里就直接插入把，实在是想不起来什么好方法判断相同，反正预测数据重新生成一遍也不是多耗时
        btcPrice.setCreatedAt(new Date());
        if (btcPrice.isPredictedPrice() && btcPrice.getPredictTime() == null){
            return -2;
        }
        return btcPriceDao.insert(btcPrice);
    }

    @Override
    public CoinPrice getOnePriceByTimeAndType(Date timePoint, Integer type) {
        // 查时间点之后（包括该点）的最近的数据
        CoinPrice price = btcPriceDao.queryOnePriceByTimeAndType(timePoint, type);
        if (price == null){
            // 如果时间点之后没有数据
            log.error("待修补：查询该时间点之前最近的数据");
        }
        return price;
    }

    @Override
    public List<CoinPrice> getPricesByTimeAndType(Date startTime, Date endTime, Integer priceType) {
        if (startTime == null) return new LinkedList<>();
        if (endTime == null) endTime = new Date();
        if (startTime.after(endTime)) return new LinkedList<>();
        if (priceType == null) priceType = CoinPrice.BITFINEX_ACTUAL_PRICE_TPYE;
        return btcPriceDao.queryPricesByTimeAndType(startTime, endTime, priceType);
    }
}
