package com.kkzz.mall.product.vo;

import com.kkzz.mall.product.entity.SkuImagesEntity;
import com.kkzz.mall.product.entity.SkuInfoEntity;
import com.kkzz.mall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {
    //1.sku基本信息获取
    //2.sku图片信息
    //4.获得spu介绍
    //3.获取sku的销售属性组合
    //5.获取spu规格参数信息
    boolean hasStock = true;
    SkuInfoEntity info;
    List<SkuImagesEntity> images;
    SpuInfoDescEntity desc;
    List<SkuItemSaleAttrsVo> saleAttr;
    List<SpuItemAttrGroupVo> groupAttrs;

    SeckillSkuRedisVo seckillInfo;

}
