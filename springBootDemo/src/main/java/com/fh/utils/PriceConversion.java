package com.fh.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceConversion{

    public static String YuanZhuanFen(String money){
        BigDecimal amount = new BigDecimal(money);//单位元
        amount = amount.multiply(new BigDecimal(100));// 单位分
        amount = amount.setScale(0, RoundingMode.DOWN);//取整
        String payAmt = String.valueOf(amount);
        return payAmt;
    }

}
