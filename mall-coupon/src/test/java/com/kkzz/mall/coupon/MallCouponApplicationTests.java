package com.kkzz.mall.coupon;


import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;


import java.util.Date;


class MallCouponApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(DateUtil.beginOfDay(new Date()).toString());
        System.out.println(DateUtil.endOfDay(DateUtil.offsetDay(new Date(), 3)));
    }

}
