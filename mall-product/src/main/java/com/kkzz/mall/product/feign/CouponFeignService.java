package com.kkzz.mall.product.feign;

import com.kkzz.common.to.SkuReductionTo;
import com.kkzz.common.to.SpuBoundTo;
import com.kkzz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mall-coupon")
public interface CouponFeignService {
    /**
     * 服务之间的数据传递 加上@RequestBody,会将数据转换为json进行传输
     * 另一服务接收到了之后,会将json重新转换为对象,也就是说
     * 传送数据服务的数据属性名和接收数据用的属性名一致即可
     * @param spuBoundTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
