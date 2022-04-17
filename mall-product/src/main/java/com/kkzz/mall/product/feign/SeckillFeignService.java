package com.kkzz.mall.product.feign;

import com.kkzz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("mall-seckill")
public interface SeckillFeignService {
    @GetMapping("/sku/seckill")
    R getSkuSeckillInfo(@RequestParam("skuId") Long skuId);
}
