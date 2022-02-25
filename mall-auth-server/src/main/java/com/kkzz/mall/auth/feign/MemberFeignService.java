package com.kkzz.mall.auth.feign;

import com.kkzz.common.utils.R;
import com.kkzz.mall.auth.vo.SocialUser;
import com.kkzz.mall.auth.vo.UserLoginVo;
import com.kkzz.mall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth2/login")
    R oauthLogin(@RequestBody SocialUser vo) throws Exception;
}
