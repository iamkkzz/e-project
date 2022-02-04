package com.kkzz.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
 *
 *
 *
 * 怎么使用逻辑删除
 *   1.(可省略)现在mybatis-plus的配置文件中配置logic-delete-field和logic-not-delete-value: 0
 *   2.在实体类上的字段上添加@TableLogic注解
 */

/**
 * 使用对象存储:
 * 1.引入依赖
 * 2.在配置文件中配置key等信息
 * 3.使用相关操作
 */

/**
 * 3.JSR303
 * 1.给Bean添加校验注解
 * 2.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }

}
