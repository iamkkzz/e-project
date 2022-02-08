package com.kkzz.mall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kkzz.mall.product.entity.BrandEntity;
import com.kkzz.mall.product.service.AttrGroupService;
import com.kkzz.mall.product.service.BrandService;
import com.kkzz.mall.product.service.CategoryService;
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

    @Autowired
    CategoryService categoryService;
    @Test
    void contextLoads() {
        Long[] catlogPath = categoryService.findCatlogPath(223L);
        for (Long aLong : catlogPath) {
            System.out.println(aLong);
        }
    }

}
