package com.kkzz.mall.member;


import com.kkzz.mall.member.service.MemberService;
import com.kkzz.mall.member.vo.MemberRegisterVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallMemberApplicationTests {

    @Autowired
    MemberService memberService;
    @Test
    void contextLoads() {
    }

}
