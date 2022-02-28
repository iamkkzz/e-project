package com.kkzz.mall.cart.controller;

import com.kkzz.mall.cart.service.CartService;
import com.kkzz.mall.cart.vo.CartItemVo;
import com.kkzz.mall.cart.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;

@Controller
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping(value = "/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        CartVo cartVo = cartService.getCart();
        model.addAttribute("cart", cartVo);
        return "cartList";
    }

    @GetMapping("/addCartItem")
    public String addCartItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num, Model model) throws ExecutionException, InterruptedException {
        cartService.addCartItem(skuId, num);
        return "redirect:http://cart.mall.com/addToCartSuccessPage.html?skuId=" + skuId;
    }

    @GetMapping("/addToCartSuccessPage.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model) {
        CartItemVo cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItemVo);
        return "success";
    }

    @GetMapping("/countItem")
    public String changeCartItemCount(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num) throws ExecutionException, InterruptedException {
        cartService.changeCount(skuId,num);
        return "redirect:http://cart.mall.com/cart.html";
    }

    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("checked") Integer check){
        cartService.check(skuId,check);
        return "redirect:http://cart.mall.com/cart.html";
    }
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId")Long skuId){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.mall.com/cart.html";
    }
}
