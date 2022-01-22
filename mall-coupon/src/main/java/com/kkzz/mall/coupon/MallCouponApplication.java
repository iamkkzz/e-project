package com.kkzz.mall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 一: 如何使用nacos配置中心统一配置管理
 *  1.导入依赖 这里注意需要导入spring-cloud-starter-bootstrap该依赖,否则bootstrap.properties无法被系统识别
 *  2.创建bootstrap.properties,并配置nacos配置中心地址和应用名称
 *  3.给配置中心默认添加 数据集(Data Id)
 *  4.给应用名.properties添加任何配置
 *  5.添加@RefreshScope和@Value("{配置名}")实现动态刷新
 *  优先使用配置中心中的配置
 *
 *  二: nacos的一些细节
 *  1.命名空间:配置隔离
 *    默认是public,默认新增的所有配置都在public空间
 *      *开发,测试,生产: 要使用某种环境,需要在bootstrap.properties文件中配置好namespace
 *      *也可以每一个微服务之间互相隔离配置,每一个微服务都创建自己的命名空间,只加载自己命名空间下的所有配置
 *  2.配置集: 所有配置的集合
 *  3.配置集id: 类似配置文件名,也就是Data Id
 *  4.配置分组: 默认所有的配置集都属于DEFAULT-GROUP,
 *      可以定义group并在bootstrap中配置spring.cloud.nacos.config.group=xxx 来确定要使用的配置文件
 * 每个微服务创建自己的命名空间,使用配置分组区分环境
 *三: 同时加载多个配置集
 *  1.微服务中的人任何配置信息,任何配置文件都可以放在配置中心中
 *  2.只需要在bootstrap.properties说明加载配置中心哪些配置文件即可
 *  3.@Value,@ConfigurationProperties等,springboot中的任何从配置文件中取值的方法都可以使用
 * 优先配置中心中有的值
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallCouponApplication.class, args);
    }

}
