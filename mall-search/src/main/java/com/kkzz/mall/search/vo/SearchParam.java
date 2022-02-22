package com.kkzz.mall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装页面传递的查询条件
 */
@Data
public class SearchParam {
    private String keyword;
    private Long catalog3Id;
    /**
     * 这个包含各色的排序,比如按价格销量热度等
     * 即sort=xxx_asc/desc
     */
    private String sort;
    /**
     * hasStock=0/1
     * skuPrice=1-500/x-500/500-x
     */
    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandId;//可能选择多个品牌
    /**
     * attrs=1_黑色:蓝色
     */
    private List<String> attrs;

    private Integer pageNum;

}
