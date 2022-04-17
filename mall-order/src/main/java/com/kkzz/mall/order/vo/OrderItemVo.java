package com.kkzz.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemVo {
    private Long skuId;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Integer count;

    //psm_spu_info表中
    private BigDecimal weight;
}
