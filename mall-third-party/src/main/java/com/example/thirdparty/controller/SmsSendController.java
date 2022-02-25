package com.example.thirdparty.controller;

import com.example.thirdparty.component.SMSComponent;
import com.kkzz.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sms")
public class SmsSendController {
    @Autowired
    SMSComponent smsComponent;

    @GetMapping("/sendcode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        smsComponent.sendSmsCode(phone,code);
        return R.ok();
    }
}
