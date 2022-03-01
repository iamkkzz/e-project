package com.kkzz.mall.order.controller;

import com.kkzz.mall.order.entity.OrderEntity;
import com.kkzz.mall.order.entity.OrderItemEntity;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RabbitController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public void send() {
        //发送消息给交换机
        for (int i = 0; i < 3; i++) {
            if (i % 2 == 0) {
                OrderItemEntity orderItemEntity = new OrderItemEntity();
                orderItemEntity.setOrderId(1L);
                rabbitTemplate.convertAndSend("hello.java.exchange", "hello", orderItemEntity,new CorrelationData(UUID.randomUUID().toString()));
            } else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello.java.exchange", "hello", orderEntity,new CorrelationData(UUID.randomUUID().toString()));
            }
        }
    }
}
