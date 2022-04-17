package com.kkzz.mall.seckill.feign;

import com.kkzz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("mall-coupon")
public interface CouponFeignService {
    @GetMapping("/coupon/seckillsession/latest3DaysSession")
    R getLatest3DaySession();
}
