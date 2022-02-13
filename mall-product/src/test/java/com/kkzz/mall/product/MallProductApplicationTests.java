package com.kkzz.mall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kkzz.mall.product.dao.SkuInfoDao;
import com.kkzz.mall.product.entity.BrandEntity;
import com.kkzz.mall.product.entity.SkuInfoEntity;
import com.kkzz.mall.product.service.AttrGroupService;
import com.kkzz.mall.product.service.BrandService;
import com.kkzz.mall.product.service.CategoryService;
import com.kkzz.mall.product.service.SkuInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    SkuInfoDao skuInfoDao;
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;
    @Test
    void contextLoads() {
        SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
        skuInfoEntity.setSpuId(25L);
        skuInfoEntity.setSaleCount(0L);
        skuInfoEntity.setBrandId(19L);
        skuInfoEntity.setCatalogId(225L);
        skuInfoEntity.setSkuName("测试");
        skuInfoEntity.setSkuSubtitle("这是一个测试");
        skuInfoEntity.setSkuTitle("能通过吗");
        skuInfoEntity.setPrice(BigDecimal.valueOf(6799));
        int i = skuInfoDao.insert(skuInfoEntity);
        if (i>0){
            System.out.println("成功11111");
        }
    }

}
