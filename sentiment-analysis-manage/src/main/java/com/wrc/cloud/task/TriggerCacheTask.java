package com.wrc.cloud.task;

import cn.hutool.core.date.DateUtil;
import com.wrc.cloud.controller.TwitterController;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.TwitterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/21 21:15
 */
@Component
public class TriggerCacheTask {

    @Resource
    private TwitterService twitterService;

    private static final Long MILLION_SECONDS_OF_ON_DAY = 24*3600*1000L;


    @Scheduled(initialDelay = 6*1000, fixedDelay = 3500*1000)
    public void requestTwitterTotalAnalysis(){
        LinkedList<String> keys = new LinkedList<>();
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - MILLION_SECONDS_OF_ON_DAY * 365);

        // 开始的一小段，到当日的24：00
        Date tmpEnd = new Date(startTime.getTime() / MILLION_SECONDS_OF_ON_DAY * MILLION_SECONDS_OF_ON_DAY
                +MILLION_SECONDS_OF_ON_DAY);
        while (startTime.before(endTime)){
            Map<String, Long> tmpResult = twitterService.updateCacheOfGetAnalysisStatisticByKeyAndTime(
                    keys,
                    tmpEnd,
                    (tmpEnd.getTime() - startTime.getTime()) / 1000);

            startTime.setTime(tmpEnd.getTime());
            tmpEnd.setTime(Math.min(tmpEnd.getTime() + MILLION_SECONDS_OF_ON_DAY, endTime.getTime()));
        }
    }
}
