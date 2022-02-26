package com.kkzz.mall.cart;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallCartApplicationTests {

    @Data
    static class People {
        private int count;
        private int age;
        private int sum;

        public int getSum() {
            return this.age * this.count;
        }
    }

    @Test
    void contextLoads() {
        People ddd = new People();
        ddd.setAge(18);
        ddd.setCount(10);
        String s = JSON.toJSONString(ddd);

        System.out.println(s);

    }

}
