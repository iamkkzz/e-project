package com.kkzz.mall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kkzz.mall.product.entity.BrandEntity;
import com.kkzz.mall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("无敌华为");
////        brandService.save(brandEntity);
////        System.out.println("保存成功!");
//        brandService.updateById(brandEntity);
//        System.out.println("修改成功!");

        List<BrandEntity> brand_id = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        brand_id.forEach(System.out::println);
    }

}
