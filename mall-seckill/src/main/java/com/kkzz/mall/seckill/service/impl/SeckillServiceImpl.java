package com.kkzz.mall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.kkzz.common.to.mq.SeckillOrderTo;
import com.kkzz.common.utils.R;
import com.kkzz.common.vo.MemberRespVo;
import com.kkzz.mall.seckill.feign.CouponFeignService;
import com.kkzz.mall.seckill.feign.ProductFeignService;
import com.kkzz.mall.seckill.interceptor.LoginInterceptor;
import com.kkzz.mall.seckill.service.SeckillService;
import com.kkzz.mall.seckill.to.SeckillSkuRedisTo;
import com.kkzz.mall.seckill.vo.SeckillSessionVo;
import com.kkzz.mall.seckill.vo.SeckillSkuRelationVo;
import com.kkzz.mall.seckill.vo.SkuInfoVo;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    RedissonClient redissonClient;
    @Autowired
    RabbitTemplate rabbitTemplate;

    private final String SESSIONS_CACHE_PREFIX = "seckill:sessions:";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";
    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    @Override
    public void uploadSeckillSkuLatest() {
        //先根据当前时间远程调用扫描最近三天开始的秒杀活动,再根据活动的id,查询该场活动关联的商品id
        R r = couponFeignService.getLatest3DaySession();
        if (r.getCode() == 0) {
            List<SeckillSessionVo> sessionVos = r.getData(new TypeReference<List<SeckillSessionVo>>() {
            });
            //缓存活动信息
            saveSessionInfos(sessionVos);
            //缓存活动商品信息
            saveSessionSkuInfos(sessionVos);
        }

    }


    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        //获取当前时间下参与秒杀的商品
        long time = new Date().getTime();
        Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        for (String key : keys) {
            String[] s = key.replace(SESSIONS_CACHE_PREFIX, "").split("_");
            long start = Long.parseLong(s[0]);
            long end = Long.parseLong(s[1]);
            if (start <= time && end >= time) {
                //获取到该场信息,获取商品信息
                List<String> skuKeys = redisTemplate.opsForList().range(key, 0, -1);
                BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                List<String> list = ops.multiGet(skuKeys);
                if (list != null) {
                    List<SeckillSkuRedisTo> redisTos = list.stream().map(item -> {
                        SeckillSkuRedisTo redisTo = JSON.parseObject(item, SeckillSkuRedisTo.class);
                        //当前秒杀已经开始,需要随机码
                        return redisTo;
                    }).collect(Collectors.toList());
                    return redisTos;
                }
                break;
            }
        }
        return null;
    }

    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        BoundHashOperations<String, String, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        SeckillSkuRedisTo skuRedisTo = new SeckillSkuRedisTo();
        Set<String> keys = ops.keys();
        for (String key : keys) {
            String[] s = key.split("_");
            if (s[1].equals(skuId.toString())) {
                String redisTo = (String) ops.get(key);
                skuRedisTo = JSON.parseObject(redisTo, SeckillSkuRedisTo.class);
                //判断是否为秒杀开始时间
                long time = new Date().getTime();
                Long startTime = skuRedisTo.getStartTime();
                Long endTime = skuRedisTo.getEndTime();
                if (time >= startTime && time <= endTime) {
                } else {
                    skuRedisTo.setRandomCode(null);
                }
                break;
            }
        }
        return skuRedisTo;
    }

    @Override
    public String kill(String killId, String key, Integer num) {
        String orderSn = null;
        MemberRespVo respVo = LoginInterceptor.loginUser.get();
        //获取redis中秒杀信息
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String redisJson = ops.get(killId);
        SeckillSkuRedisTo skuRedisTo = JSON.parseObject(redisJson, SeckillSkuRedisTo.class);
        //判断时间是否过期
        long time = new Date().getTime();
        Long startTime = skuRedisTo.getStartTime();
        Long endTime = skuRedisTo.getEndTime();
        long ttl = endTime - time;
        if (time >= startTime && time <= endTime) {
            //处于秒杀时间内
            String randomCode = skuRedisTo.getRandomCode();
            if (randomCode.equals(key)) {
                //验证码正确
                if (num <= skuRedisTo.getSeckillLimit().intValue()) {
                    //购买数量符合
                    //判断是否已经购买
                    String redisKey = respVo.getId() + "_" + killId;
                    Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                    if (aBoolean) {
                        //占位成功,则进行购买
                        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                        boolean tryAcquire = semaphore.tryAcquire(num);
                        //秒杀成功,快速下单,发送mq消息
                        if (tryAcquire) {
                            orderSn = IdWorker.getTimeId();
                            SeckillOrderTo orderTo = new SeckillOrderTo();
                            orderTo.setOrderSn(orderSn);
                            orderTo.setNum(num);
                            orderTo.setMemberId(respVo.getId());
                            orderTo.setSeckillPrice(skuRedisTo.getSeckillPrice());
                            orderTo.setPromotionSessionId(skuRedisTo.getPromotionId());
                            orderTo.setSkuId(skuRedisTo.getSkuId());
                            rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", orderTo);
                        }
                    }
                }
            }
        }
        return orderSn;
    }

    private void saveSessionInfos(List<SeckillSessionVo> sessionVos) {
        sessionVos.stream().forEach(session -> {
            long start = session.getCreateTime().getTime();
            long end = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + start + "_" + end;
            Boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey) {
                List<String> collect = session.getRelationSkus().stream().map(item -> item.getPromotionSessionId() + "_" + item.getSkuId()).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key, collect);
            }
        });
    }

    private void saveSessionSkuInfos(List<SeckillSessionVo> sessionVos) {
        //再根据商品id,查询商品信息
        sessionVos.stream().forEach(session -> {
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            List<Long> skuIds = session.getRelationSkus().stream().map(skuVo -> skuVo.getSkuId()).collect(Collectors.toList());
            //利用远程调用一次性查询到所有的商品信息
            R r = productFeignService.skusInfo(skuIds);
            if (r.getCode() == 0) {
                Map<Long, SkuInfoVo> skuInfoVoMap = r.getData(new TypeReference<Map<Long, SkuInfoVo>>() {
                });
                session.getRelationSkus().forEach(seckillSkuRelationVo -> {
                    String randomCode = UUID.randomUUID().toString().replace("-", "");
                    Boolean key = ops.hasKey(seckillSkuRelationVo.getPromotionSessionId() + "_" + seckillSkuRelationVo.getSkuId());
                    if (!key) {
                        SeckillSkuRedisTo skuRedisTo = new SeckillSkuRedisTo();
                        //设置sku详细信息
                        skuRedisTo.setSkuInfo(skuInfoVoMap.get(seckillSkuRelationVo.getSkuId()));
                        BeanUtils.copyProperties(seckillSkuRelationVo, skuRedisTo);
                        //设置活动的开始时间和结束时间
                        skuRedisTo.setStartTime(session.getStartTime().getTime());
                        skuRedisTo.setEndTime(session.getEndTime().getTime());
                        //设置商品的随机码
                        skuRedisTo.setRandomCode(randomCode);
                        String redisString = JSON.toJSONString(skuRedisTo);
                        ops.put(seckillSkuRelationVo.getPromotionSessionId() + "_" + seckillSkuRelationVo.getSkuId(), redisString);
                        //设置信号量,进行限流,如果当前场次没有设置则进行
                        if (!redisTemplate.hasKey(SKU_STOCK_SEMAPHORE + randomCode)) {
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            semaphore.trySetPermits(seckillSkuRelationVo.getSeckillCount().intValue());
                        }
                    }
                });
            }
        });
    }
}
