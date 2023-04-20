package com.wrc.cloud.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.io.Serializable;

/**
 * Price实体类
 *
 * @author wrc
 * @since 2023-04-09 17:01:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CoinPrice implements Serializable {
    private static final long serialVersionUID = -27157709913186247L;

    // 实际价格的类型值
    public static final Integer BITFINEX_ACTUAL_PRICE_TPYE = 100;
    // 预测型价格的最大值
    public static final Integer PREDICT_TYPE_MAX = 99;

    private BigInteger id;
    /**
     * 该价格对应的时间节点
     */
    private Date timePoint;
    /**
     * 以美元为单位
     */
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;
    /**
     * 预测的价格0，各种模型的预测数据在1-100间
     * 实际平均价100，各个交易所的数据在100+
     *
     * LSTM + sentiment 就是0
     * 仅使用LSTM的预测是 1
     *
     * 或者其他表现的价格，也可以是不同模型预测的价格
     */
    private Integer priceType;
    /**
     * 做出预测的时间，也就是通过哪个时间点前的数据做的预测。type不是预测类型 的话，本属性为NULL
     */
    private Date predictTime;
    /**
     * 本记录创建时间，与每条记录的内容无关
     */
    private Date createdAt;


    public static boolean isPredictedPrice(CoinPrice price){
        if (price.priceType == null) return false;
        if (price.priceType <= PREDICT_TYPE_MAX) return true;
        return false;
    }

    public static boolean isEmptyPrice(CoinPrice price) {
        if (price.getId() == null) return true;
        return false;
    }
}

