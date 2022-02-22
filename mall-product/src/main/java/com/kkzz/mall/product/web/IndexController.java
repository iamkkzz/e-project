package com.kkzz.mall.product.web;

import com.kkzz.mall.product.entity.CategoryEntity;
import com.kkzz.mall.product.service.CategoryService;
import com.kkzz.mall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //1.todo 查出所有一级分类
        List<CategoryEntity> category = categoryService.getLevel1Category();
        model.addAttribute("categorys", category);
        return "index";
    }

    //"index/catalog.json"
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        //1.获取锁,只要锁的名字一样就是同一把锁
        RLock lock = redisson.getLock("my-lock");
        //2.加锁,默认加锁30s
        //解决了锁的自动续期,如果业务超长,运行期间自动给锁续上新的时间.所以不用担心业务时间长,锁的自动过期被删除
        //加锁的业务只要运行完成,就不会给当前锁续期,即使不手动解锁,锁也会在默认30s后自动删除
//        lock.lock(10, TimeUnit.SECONDS);//10s自动解锁
        lock.lock();
        try {
            System.out.println("加锁成功,执行业务" + Thread.currentThread().getName());
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //解锁
            lock.unlock();
            System.out.println("解锁成功,释放业务" + Thread.currentThread().getName());
        }
        return "hello";
    }

    //保证一定可以读到最新数据,修改期间,写锁是一个排他锁
    //并发读只会在redis中记录好读锁,他们都会同时加锁成功
    @GetMapping("/write")
    @ResponseBody
    public String writeValue() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        RLock rLock = lock.writeLock();
        String s = "";
        try {
            rLock.lock();
            s = UUID.randomUUID().toString();
            System.out.println("写锁成功!!!");
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue",s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            rLock.unlock();
        }

        return s;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        String s = "";
        RLock rLock = lock.readLock();
        rLock.lock();
        try {
            System.out.println("读锁成功!!!");
            Thread.sleep(10000);
            s = redisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            rLock.unlock();
        }
        return s;
    }
}
