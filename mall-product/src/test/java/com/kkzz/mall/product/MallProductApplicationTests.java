package com.kkzz.mall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kkzz.mall.product.config.MyRedissonConfig;
import com.kkzz.mall.product.dao.AttrGroupDao;
import com.kkzz.mall.product.dao.SkuInfoDao;
import com.kkzz.mall.product.dao.SkuSaleAttrValueDao;
import com.kkzz.mall.product.entity.BrandEntity;
import com.kkzz.mall.product.entity.SkuInfoEntity;
import com.kkzz.mall.product.service.AttrGroupService;
import com.kkzz.mall.product.service.BrandService;
import com.kkzz.mall.product.service.CategoryService;
import com.kkzz.mall.product.service.SkuInfoService;
import com.kkzz.mall.product.vo.SkuItemSaleAttrsVo;
import com.kkzz.mall.product.vo.SkuItemVo;
import com.kkzz.mall.product.vo.SpuItemAttrGroupVo;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
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
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    SkuSaleAttrValueDao saleAttrValueDao;
    @Test
    void contextLoads() {
        List<SkuItemSaleAttrsVo> saleAttrsBySpuId = saleAttrValueDao.getSaleAttrsBySpuId(38L);
        System.out.println(saleAttrsBySpuId.toString());
    }
    @Test
    void redisTest(){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("hello","world"+ UUID.randomUUID().toString());
        System.out.println(ops.get("hello"));
    }

    @Test
    void redissonTest(){
        System.out.println(redissonClient.toString());
    }
}
