package com.kkzz.mall.seckill.feign;

import com.kkzz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("mall-product")
public interface ProductFeignService {
    @GetMapping("/product/skuinfo/infos")
    R skusInfo(@RequestParam List<Long> skuIds);
}
