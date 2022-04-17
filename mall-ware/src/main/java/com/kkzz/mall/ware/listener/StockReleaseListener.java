package com.kkzz.mall.ware.listener;

import com.alibaba.fastjson.TypeReference;
import com.kkzz.common.to.mq.OrderTo;
import com.kkzz.common.to.mq.StockLockedTo;
import com.kkzz.common.utils.R;
import com.kkzz.mall.ware.entity.WareOrderTaskDetailEntity;
import com.kkzz.mall.ware.entity.WareOrderTaskEntity;
import com.kkzz.mall.ware.feign.OrderFeignService;
import com.kkzz.mall.ware.service.WareOrderTaskDetailService;
import com.kkzz.mall.ware.service.WareOrderTaskService;
import com.kkzz.mall.ware.service.WareSkuService;
import com.kkzz.mall.ware.vo.OrderVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        try {
            wareSkuService.unlockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }

    @RabbitHandler
    public void handleOrderRelease(OrderTo to, Message message, Channel channel) throws IOException {
        System.out.println("收到订单关闭的消息,准备解锁库存");
        try {
            wareSkuService.unlockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
