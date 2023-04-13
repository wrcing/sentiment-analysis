package com.wrc.cloud.convert;

import cn.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/13 22:41
 */
@Component
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        try {
            Long time = Long.valueOf(source);
            Date date = new Date(time);
            return date;
        } catch (NumberFormatException e){
            //使用Hutool工具类转日期
            return DateUtil.parse(source);
        }catch (Exception e) {
            return null;
        }
    }

}
