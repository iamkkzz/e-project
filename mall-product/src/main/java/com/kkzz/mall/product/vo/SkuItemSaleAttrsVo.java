package com.kkzz.mall.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class SkuItemSaleAttrsVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}
