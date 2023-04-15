package com.wrc.cloud.test;

import com.wrc.cloud.PO.TweetPO;
import com.wrc.cloud.service.TwitterService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/1 15:07
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TestTwitter {

//    @Autowired
    private TwitterService twitterService;

//    @Test
    public void TestTwitterQuery() throws ParseException {
        //测试 比特币相关数据查询
        List<String> list = new LinkedList<>();
        list.add("bitcoin");


        String beginTime = "2023-04-01 14:42:32";
        String endTime = "2023-03-20 12:26:32";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = format.parse(beginTime);
        Date date2 = format.parse(endTime);
        List<TweetPO> result = twitterService.getTweetsByTimeAndTexts(date1, date2, list);
        System.out.println(result);
    }
}
