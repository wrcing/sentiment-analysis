package com.wrc.cloud.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wrc.cloud.PO.CoinPrice;
import com.wrc.cloud.service.BTCPriceService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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

    /**
     * 每个小时
     * 从交易所拉取的价格数据
     * */
    @Scheduled(cron = "30 10 * * * ?")
    public void pullBTCPriceFromBitfinex() throws IOException {
        Date currentTime = new Date(new Date().getTime() / 60000 * 60000);
        List<CoinPrice> prices = getRTCPricesOfMinute(currentTime, 61);
        for (CoinPrice price : prices){
            priceService.saveBTCPrice(price);
        }
    }

    private List<CoinPrice> getRTCPricesOfMinute(Date endTime, Integer limit) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://try.readme.io/https://api-pub.bitfinex.com/v2/candles/trade%3A1m%3AtBTCUSD/hist?end=1682685600000&limit=60")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Host", "try.readme.io")
                .addHeader("Origin", "https://docs.bitfinex.com")
                .addHeader("Referer", "https://docs.bitfinex.com/")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();

        String pricesStr = response.body() == null ? "" : response.body().string();
        LinkedList<CoinPrice> result = new LinkedList<>();
        for (String price : pricesStr.split("]")){
            if (price.startsWith("[[")) {
                price = price.substring(2);
            }
            if (price.startsWith(",[")){
                price = price.substring(2);
            }
            if (price.equals("]")) continue;
            String[] priceData = price.split(",");
            CoinPrice p = new CoinPrice();
            p.setTimePoint(new Date(Long.parseLong(priceData[0])));
            p.setOpen(new BigDecimal(priceData[1]));
            p.setClose(new BigDecimal(priceData[2]));
            p.setHigh(new BigDecimal(priceData[3]));
            p.setLow(new BigDecimal(priceData[4]));
            p.setVolume(new BigDecimal(priceData[5]));

            p.setPriceType(CoinPrice.BITFINEX_ACTUAL_PRICE_TPYE);
            result.add(p);
        }
        return result;
    }


    /**
     * 每天，补齐以往的数据（2023年以来）
     * 从交易所拉取的价格数据
     * */
    @Scheduled(cron = "30 0 1 * * ?")
    public void pullBTCPriceFromBitfinexFrom2023() throws IOException, InterruptedException {
        Date startTime = DateUtil.parse("2023-04-28 20:00:00");
        Date currentTime = new Date();
        int count = 0;
        while (startTime.before(currentTime)){
            CoinPrice existed = priceService.getOnePriceByTimeAndType(startTime, CoinPrice.BITFINEX_ACTUAL_PRICE_TPYE);
            if (existed != null && !startTime.equals(existed.getTimePoint())){
                // 该小时点的数据，在数据库内不存在
                List<CoinPrice> prices = getRTCPricesOfMinute(startTime, 61);
                for (CoinPrice price : prices){
                    int i = priceService.saveBTCPrice(price);
                    if (i > 0) count++;
                }
            }

            startTime.setTime(startTime.getTime() + MILLION_SECONDS_ONE_HOUR);
            Thread.sleep(2000);
        }
        log.info("拉取历史btc价格 {}条", count);
    }
}
