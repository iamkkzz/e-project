package com.kkzz.mall.ware;

import com.kkzz.mall.ware.dao.WareSkuDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class MallWareApplicationTests {

    @Autowired
    WareSkuDao wareSkuDao;
    @Test
    void contextLoads() {
        Integer integer = wareSkuDao.lockSkuStock(38L, 3, 1L);
        System.out.println(integer);
    }

}
