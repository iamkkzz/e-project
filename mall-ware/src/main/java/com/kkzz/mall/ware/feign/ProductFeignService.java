package com.kkzz.mall.ware.feign;

import com.kkzz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-product")
public interface ProductFeignService {
    /**
     * 这里有两种
     * 1.让所有请求过网关,即给网关服务发送请求/api/product/skuinfo/info/{skuId}
     * 2.直接给指定服务发送请求
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
