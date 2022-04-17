package com.kkzz.mall.seckill.scheduled;

import com.kkzz.mall.seckill.service.SeckillService;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SeckillSkuScheduled {
    @Autowired
    SeckillService seckillService;

    @Autowired
    RedissonClient redissonClient;

    private final String upload_lock = "seckill:upload:lock";

    /**
     * 每天晚上三点,上架最近三天需要秒杀的商品
     */
    @Async
    @Scheduled(cron = "* * 5 * * ? ")
    public void uploadSeckillSkuLatest() {
        //重复上架无需处理
        System.out.println("我执行了");
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillService.uploadSeckillSkuLatest();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
