package com.kkzz.mall.order.feign;

import com.kkzz.common.utils.R;
import com.kkzz.mall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("mall-member")
public interface MemberFeignService {
    @RequestMapping("/member/memberreceiveaddress/{memberId}/address")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);
}
