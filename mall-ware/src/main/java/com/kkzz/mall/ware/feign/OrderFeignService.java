package com.kkzz.mall.ware.feign;

import com.kkzz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("mall-order")
public interface OrderFeignService {
    @GetMapping("/order/order/status/{orderSn}")
    R getStatus(@PathVariable("orderSn") String orderSn);
}
