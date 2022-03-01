package com.kkzz.mall.order;

import com.kkzz.mall.order.entity.OrderEntity;
import com.kkzz.mall.order.entity.OrderItemEntity;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class MallOrderApplicationTests {


    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void create() {
        //创建交换机
        DirectExchange directExchange = new DirectExchange("hello.java.exchange", true, false);
        rabbitAdmin.declareExchange(directExchange);
        //创建队列
        Queue queue1 = new Queue("queue.hello", true, false, false);
        rabbitAdmin.declareQueue(queue1);
        Queue queue2 = new Queue("queue.world", true, false, false);
        rabbitAdmin.declareQueue(queue2);
        //绑定交换机队列
        Binding binding1 = new Binding("queue.hello", Binding.DestinationType.QUEUE, "hello.java.exchange",
                "hello", null);
        rabbitAdmin.declareBinding(binding1);
        Binding binding2 = new Binding("queue.world", Binding.DestinationType.QUEUE, "hello.java.exchange",
                "world", null);
        rabbitAdmin.declareBinding(binding2);
    }

    @Test
    void send() {
        //发送消息给交换机
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                OrderItemEntity orderItemEntity = new OrderItemEntity();
                orderItemEntity.setOrderId(1L);
                rabbitTemplate.convertAndSend("hello.java.exchange", "hello", orderItemEntity);
            }else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello.java.exchange", "hello", orderEntity);
            }
        }
    }

}
