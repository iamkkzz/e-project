package com.kkzz.mall.cart.service;

import com.kkzz.mall.cart.vo.CartItemVo;
import com.kkzz.mall.cart.vo.CartVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CartService {
    CartItemVo addCartItem(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartVo getCart() throws ExecutionException, InterruptedException;

    CartItemVo getCartItem(Long skuId);

    void clearCart(String cartKey);

    void changeCount(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    void check(Long skuId, Integer check);

    void deleteItem(Long skuId);

    List<CartItemVo> getCurrentCartItems();
}
