package com.kkzz.mall.order.web;

import com.kkzz.common.exception.NoStockException;
import com.kkzz.mall.order.service.OrderService;
import com.kkzz.mall.order.vo.OrderConfirmVo;
import com.kkzz.mall.order.vo.OrderRespVo;
import com.kkzz.mall.order.vo.OrderSubmitVo;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    //进入结算页面
    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("confirmOrderData", confirmVo);
        return "confirm";
    }

    //进入付款页面
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes attributes) {
        try {
            OrderRespVo respVo = orderService.submitOrder(vo);
            if (respVo.getCode() == 0) {
                //下单成功返回支付页面
                model.addAttribute("submitOrderResp", respVo);
                return "pay";
            } else {
                //下单失败则返回订单确认页面
                String msg = "下单失败";
                switch (respVo.getCode()) {
                    case 1:
                        msg += "令牌订单信息过期，请刷新再次提交";
                        break;
                    case 2:
                        msg += "订单商品价格发生变化，请确认后再次提交";
                        break;
                    case 3:
                        msg += "库存锁定失败，商品库存不足";
                        break;
                }
                attributes.addFlashAttribute("msg", msg);
                return "redirect:http://order.mall.com/toTrade";
            }
        } catch (Exception e) {
            if (e instanceof NoStockException) {
                String message = ((NoStockException) e).getMessage();
                attributes.addFlashAttribute("msg", message);
            }
            return "redirect:http://order.mall.com/toTrade";
        }
    }
}
