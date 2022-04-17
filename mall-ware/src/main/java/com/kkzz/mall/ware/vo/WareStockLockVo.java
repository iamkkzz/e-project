package com.kkzz.mall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class WareStockLockVo {
    String orderSn;
    List<OrderItemVo> locks;
}
