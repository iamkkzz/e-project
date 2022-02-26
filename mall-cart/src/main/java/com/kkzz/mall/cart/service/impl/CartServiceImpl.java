package com.kkzz.mall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kkzz.common.utils.R;
import com.kkzz.mall.cart.feign.ProductFeignService;
import com.kkzz.mall.cart.interceptor.CartInterceptor;
import com.kkzz.mall.cart.service.CartService;
import com.kkzz.mall.cart.vo.CartItemVo;
import com.kkzz.mall.cart.vo.CartVo;
import com.kkzz.mall.cart.vo.SkuInfoVo;
import com.kkzz.mall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;
    public static final String CART_PREFIX = "mall:cart:";

    /**
     * 将商品信息存入Redis
     *
     * @param skuId
     * @param num
     */
    @Override
    public CartItemVo addCartItem(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //远程调用商品服务查询商品详细信息
        CartItemVo cartItemVo = new CartItemVo();
        String redisCart = (String) cartOps.get(skuId.toString());
        String cartJson = "";
        if (redisCart == null) {
            //开启异步 节省时间
            CompletableFuture<Void> infoFuture = CompletableFuture.runAsync(() -> {
                R info = productFeignService.info(skuId);
                if (info.getCode() == 0) {
                    //查询成功,取出数据装入
                    SkuInfoVo skuinfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                    });
                    cartItemVo.setCount(num);
                    cartItemVo.setImage(skuinfo.getSkuDefaultImg());
                    cartItemVo.setPrice(skuinfo.getPrice());
                    cartItemVo.setTitle(skuinfo.getSkuTitle());
                    cartItemVo.setSkuId(skuId);
                }
            }, executor);

            CompletableFuture<Void> valueFuture = CompletableFuture.runAsync(() -> {
                R r = productFeignService.getAttrValue(skuId);
                if (r.getCode() == 0) {
                    List<String> skuSaleAttrValue = r.getData("data", new TypeReference<List<String>>() {
                    });
                    cartItemVo.setSkuAttr(skuSaleAttrValue);
                }
            }, executor);
            CompletableFuture.allOf(infoFuture, valueFuture).get();
            //放入数据库
            cartJson = JSON.toJSONString(cartItemVo);
        } else {
            //如果存在redis中,则只需要改变数量
            CartItemVo updateCart = JSON.parseObject(redisCart, CartItemVo.class);
            updateCart.setCount(updateCart.getCount() + num);
            //转换为JSON再次进行存储
            cartJson = JSON.toJSONString(updateCart);
        }
        cartOps.put(skuId.toString(), cartJson);
        return cartItemVo;
    }

    @Override
    public CartVo getCart() {
//        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
//        //分为登录和未登录,未登录就是直接获得所有的,登录了则需要进行合并
//        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
//        if (userInfoTo.getUserId()==null){
//            //未登录获取所有的商品信息,因为已经绑定
//            cartOps.multiGet()
//        }else {
//
//        }
        return null;
    }

    @Override
    public CartItemVo getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String redisCartItem = (String) cartOps.get(skuId.toString());
        CartItemVo cartItemVo = JSON.parseObject(redisCartItem, CartItemVo.class);
        return cartItemVo;
    }

    /**
     * 获得当前用户或者临时用户的购物车
     *
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() == null) {
            //临时购物车
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        } else {
            //登录后的购物车
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        }
        //需要判断购物车中是否含有这件商品,没有添加,有则修改
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }
}
