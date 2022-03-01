package com.kkzz.mall.order.feign;

import com.kkzz.mall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient("mall-cart")
public interface CartFeignService {
    @GetMapping(value = "/currentUserCartItems")
    List<OrderItemVo> getCurrentCartItems();
}
