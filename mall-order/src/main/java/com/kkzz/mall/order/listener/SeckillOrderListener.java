package com.kkzz.mall.order.listener;

import com.kkzz.common.to.mq.SeckillOrderTo;
import com.kkzz.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RabbitListener(queues = "order.seckill.order.queue")
@Component
public class SeckillOrderListener {
    @Autowired
    OrderService orderService;
    @RabbitHandler
    public void listener(SeckillOrderTo to, Message message, Channel channel) throws IOException {
        //进行关闭订单的业务
        System.out.println("收到消息,执行快速订单创建.....");
        try {
            orderService.createSeckillOrder(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
