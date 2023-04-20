package com.wrc.cloud.service;

import com.wrc.cloud.PO.CoinPrice;

import java.util.Date;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/9 17:37
 */
public interface BTCPriceService {

    int saveBTCPrice(CoinPrice btcPrice);

    CoinPrice getOnePriceByTimeAndType(Date timePoint, Integer type);


    /**
     * 价格类型为null的话，就返回实际数据
     * */
    List<CoinPrice> getPricesByTimeAndType(Date startTime, Date endTime, Integer priceType, Long sepSeconds);

}
