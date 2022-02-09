package com.kkzz.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kkzz.common.utils.PageUtils;
import com.kkzz.mall.product.entity.BrandEntity;
import com.kkzz.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;


public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);

}

