package com.kkzz.mall.order.vo;

import lombok.Data;

import java.util.List;

@Data
public class WareStockLockVo {
    String orderSn;
    List<OrderItemVo> locks;
}
