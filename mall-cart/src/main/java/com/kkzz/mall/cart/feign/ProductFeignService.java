package com.kkzz.mall.cart.feign;

import com.kkzz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient("mall-product")
public interface ProductFeignService {
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    @RequestMapping("/product/skusaleattrvalue/list/getattrvalue/{skuId}")
    R getAttrValue(@PathVariable("skuId") Long skuId);

    @RequestMapping("/product/skuinfo/price")
    Map<Long, BigDecimal> getNewPrice(@RequestParam List<Long> ids);
}
