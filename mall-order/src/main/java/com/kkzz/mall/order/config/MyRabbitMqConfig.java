package com.kkzz.mall.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitMqConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //该类对象创建完成构造器调用以后进行调用
    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 只要消息抵达消息代理的交换机,即为成功
             * @param correlationData 当前消息的唯一关联数据,含有消息的ID和内容
             * @param ack 是否成功收到
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("消息实体为: " + correlationData + ",ack为: " + ack + ",失败原因是: " + cause);

            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 设置消息抵达队列的确认回调,没有抵达则触发
             * @param message
             * @param replyCode
             * @param replyText
             * @param exchange
             * @param routingKey
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println(
                        "Fail message: " + message + "replyCode: " + replyCode + "replyText: " + replyText +
                                "exchange: " + exchange + "routingKey: " + routingKey
                );
            }
        });

    }
}
