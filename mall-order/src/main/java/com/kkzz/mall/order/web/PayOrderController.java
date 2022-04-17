package com.kkzz.mall.order.web;

import com.alipay.api.AlipayApiException;
import com.kkzz.mall.order.config.AlipayTemplate;
import com.kkzz.mall.order.service.OrderService;
import com.kkzz.mall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PayOrderController {
    @Autowired
    AlipayTemplate alipayTemplate;

    @Autowired
    OrderService orderService;
    @GetMapping(value = "/aliPayOrder",produces = "text/html")
    @ResponseBody
    public String aliPay(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVo payVo=orderService.getOrderPay(orderSn);
        String pay = alipayTemplate.pay(payVo);
        return pay;
    }
}
