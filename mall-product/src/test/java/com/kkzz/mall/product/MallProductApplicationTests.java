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
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    SkuInfoDao skuInfoDao;
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate redisTemplate;
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
    @Test
    void redisTest(){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("hello","world"+ UUID.randomUUID().toString());
        System.out.println(ops.get("hello"));
    }

}
