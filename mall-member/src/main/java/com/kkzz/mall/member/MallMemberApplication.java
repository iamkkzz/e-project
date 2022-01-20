package com.kkzz.mall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 想要远程调用别的事务
 *  1.引入open-feign
 *  2.编写接口,告诉springcloud这个接口需要调用远程服务
 *      1.在接口中声明要调用的方法是哪个远程服务的哪个请求
 *  3.开启远程调用功能
 */
@EnableFeignClients(basePackages = "com.kkzz.mall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallMemberApplication.class, args);
    }

}
