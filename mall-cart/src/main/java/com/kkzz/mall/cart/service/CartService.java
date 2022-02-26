package com.kkzz.mall.cart.service;

import com.kkzz.mall.cart.vo.CartItemVo;
import com.kkzz.mall.cart.vo.CartVo;

import java.util.concurrent.ExecutionException;

public interface CartService {
    CartItemVo addCartItem(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartVo getCart();

    CartItemVo getCartItem(Long skuId);
}
