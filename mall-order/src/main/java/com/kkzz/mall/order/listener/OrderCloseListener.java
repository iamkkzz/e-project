package com.kkzz.mall.order.listener;

import com.kkzz.mall.order.entity.OrderEntity;
import com.kkzz.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RabbitListener(queues = "order.release.order.queue")
@Service
public class OrderCloseListener {


    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listener(Message message, Channel channel, OrderEntity order) throws IOException {
        //进行关闭订单的业务
        System.out.println("收到消息,执行关闭订单业务.....");
        try {
            orderService.closeOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
