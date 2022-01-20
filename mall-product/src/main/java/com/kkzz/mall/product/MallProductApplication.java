package com.kkzz.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1.整合mybatis-plus
 *  1) 导入依赖
 *  2) 配置
 *      1.配置数据源
 *          导入驱动
 *          配置数据源相关信息
 *      2.配置mybatis-plus
 *          使用mapperScan
 *          配置sql映射文件位置
 */
@SpringBootApplication
public class MallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }

}
