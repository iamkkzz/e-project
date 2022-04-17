package com.kkzz.mall.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyMQConfig {

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "order-event-exchange");
        args.put("x-dead-letter-routing-key", "order.release.order");
        args.put("x-message-ttl", 60000);
        return new Queue("order.delay.queue", true, false, false, args);
    }

    @Bean
    public Queue orderReleaseQueue() {
        return new Queue("order.release.order.queue", true, false, false);
    }

    @Bean
    public Binding orderCreateOrderBinding() {
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.create.order", null);
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("order.release.order.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.order", null);
    }

    /**
     * 本来是订单关闭之后,等待一段时间,库存服务查询订单状态判断进行业务;但是如果订单正在关闭,库存服务就已经判断了,那就无法做到正常业务
     * 所以我们在订单关闭之后,手动给库存服务的发送消息
     *
     * @return
     */
    @Bean
    public Binding orderReleaseOtherBinding() {
        return new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.other.#", null);
    }

    @Bean
    public Queue orderSeckillOrderQueue(){
        return new Queue("order.seckill.order.queue",true,false,false);
    }

    @Bean
    public Binding orderSeckillBinding(){
        return new Binding("order.seckill.order.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange","order.seckill.order",null);
    }
}
