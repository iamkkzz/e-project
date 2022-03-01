package com.kkzz.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmVo {
    //用户地址信息
    List<MemberAddressVo> address;
    //选中商品信息
    List<OrderItemVo> items;
    //优惠信息
    Integer integration;
    //订单总额
    BigDecimal total;
    //应付价格
    BigDecimal payPrice;
    //防重令牌
    String orderToken;

    public BigDecimal getTotal() {
        BigDecimal totalNum = BigDecimal.ZERO;
        if (items != null && items.size() > 0) {
            for (OrderItemVo item : items) {
                //计算当前商品的总价格
                BigDecimal itemPrice = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                //再计算全部商品的总价格
                totalNum = totalNum.add(itemPrice);
            }
        }
        return totalNum;
    }

    public BigDecimal getPayPrice() {
        return getTotal();
    }
}
