package com.kkzz.mall.order.vo;

import com.kkzz.mall.order.entity.OrderEntity;
import lombok.Data;

@Data
public class OrderRespVo {
    private OrderEntity order;

    /** 错误状态码 **/
    private Integer code;

}
