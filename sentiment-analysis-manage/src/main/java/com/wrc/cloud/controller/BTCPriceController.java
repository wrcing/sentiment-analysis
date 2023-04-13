package com.wrc.cloud.controller;

import com.wrc.cloud.PO.CoinPrice;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.BTCPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/9 17:40
 */
@Slf4j
@RestController
@RequestMapping("/api/btc")
public class BTCPriceController {

    @Resource
    private BTCPriceService btcPriceService;

    /**
     * 保存 btc 的一条price
     *
     * priceType参见 CoinPrice.priceType 的设计
     * */
    @PostMapping("/price")
    public ResponseResult<Integer> saveBTCPrice(@RequestBody CoinPrice btcPrice)  {
//        System.out.println(btcPrice.toString());
        int result = btcPriceService.saveBTCPrice(btcPrice);
        if (result != 1) return ResponseResult.error(String.valueOf(result));
        return ResponseResult.success(result);
    }

    @GetMapping("/price")
    public ResponseResult<CoinPrice> getSingleBTCPrice(@RequestParam("timePoint") Date startTime,
                                                       @RequestParam("type") Integer priceType){

        CoinPrice price = btcPriceService.getOnePriceByTimeAndType(startTime, priceType);
        return ResponseResult.success(price);
    }

    /**
     * 获取一段时间的 btc price
     * 要有时间参数
     * 价格类型参数，是预测还是实际，返回 type字段 就行
     * */
    @GetMapping("/prices")
    public ResponseResult<List<CoinPrice>> getBTCPrice(@RequestParam("startTime") Date startTime,
                                                       @RequestParam("endTime") Date endTime,
                                                       @RequestParam("type") Integer priceType){
        List<CoinPrice> prices = btcPriceService.getPricesByTimeAndType(startTime, endTime, priceType);
        return ResponseResult.success(prices);
    }
}
