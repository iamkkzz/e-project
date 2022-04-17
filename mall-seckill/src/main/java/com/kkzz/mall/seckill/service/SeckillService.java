package com.kkzz.mall.seckill.service;

import com.kkzz.mall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

public interface SeckillService {

    void uploadSeckillSkuLatest();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    String kill(String  killId, String key, Integer num);
}
