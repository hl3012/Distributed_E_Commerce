package com.kk.onlineshopping.service.mq;

import com.alibaba.fastjson.JSON;
import com.kk.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.kk.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.kk.onlineshopping.db.po.OnlineShoppingOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@RocketMQMessageListener(topic = "createOrder", consumerGroup = "createOrderGroup")
public class CreateOrderListener implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {
    @Resource
    OnlineShoppingCommodityDao onlineShoppingCommodityDao;
    @Resource
    OnlineShoppingOrderDao onlineShoppingOrderDao;
    @Resource
    RocketMQService rocketMQService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        //convert msg to order
        //insert commodity
        //insert into order table
        OnlineShoppingOrder order = JSON.parseObject(msg, OnlineShoppingOrder.class);
        long commodityId = order.getCommodityId();
        long userId = order.getUserId();
        int result = onlineShoppingCommodityDao.deductStockWithCommodityId(commodityId);
        if (result == 1) {
            onlineShoppingOrderDao.insertOrder(order);
            rocketMQService.sendDelayMessage("paymentCheck", JSON.toJSONString(order), 4);
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setMaxReconsumeTimes(2);
        consumer.setConsumeTimeout(5);
        consumer.setConsumeThreadMin(2);
        consumer.setConsumeThreadMax(2);
    }
}
