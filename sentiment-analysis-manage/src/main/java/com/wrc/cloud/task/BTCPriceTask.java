package com.wrc.cloud.task;

import cn.hutool.core.date.DateUtil;
import com.wrc.cloud.PO.CoinPrice;
import com.wrc.cloud.service.BTCPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/17 21:38
 */
@Component
@Slf4j
public class BTCPriceTask {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private BTCPriceService priceService;

    private static final Long MILLION_SECONDS_ONE_HOUR = 3600*1000L;

    /**
     * 每个小时点 检查以往的预测数据
     * 往前数每个小时点，没有预测数据便加
     * btc价格
     * */
    @Scheduled(cron = "5 0 * * * ?")
    public void sendBTCPricePredictRequest(){
        Date lastHourTime = new Date((new Date().getTime() / MILLION_SECONDS_ONE_HOUR ) * MILLION_SECONDS_ONE_HOUR
                + MILLION_SECONDS_ONE_HOUR + 1);
        Date earlyHourTime = DateUtil.parse("2023-01-01 00:00:00");

        Map<String, Object> map = new HashMap<>();
        long absencePriceNum = 0L;
        while (earlyHourTime.before(lastHourTime)){
            // 真的从数据库中读取，非缓存
            if (CoinPrice.isEmptyPrice(priceService.getOnePriceByTimeAndTypeWithCacheUpdate(earlyHourTime, 0))){
                // 数据库中 无该时间点的预测数据
                // log.info("无btc价格预测："+DateUtil.format(lastHourTime, "yyyy-MM-dd HH:mm:ss"));
                absencePriceNum += 1;

                map.clear();
                map.put("realPriceType", CoinPrice.BITFINEX_ACTUAL_PRICE_TPYE);
                // 要对哪个时间点 做出预测
                map.put("priceTimePoint", earlyHourTime.getTime());
                // 以哪个时间点的数据做出预测
                map.put("predictTime", new Date(earlyHourTime.getTime() - MILLION_SECONDS_ONE_HOUR));

                rabbitTemplate.convertAndSend(
                        "Cloud.AnalysisExchange",
                        "PricePredictRouteBTC",
                        map);
            }
            earlyHourTime = new Date(earlyHourTime.getTime() + MILLION_SECONDS_ONE_HOUR);
        }
        log.info("缺少 {}个 价格预测数据", absencePriceNum);
    }
}
