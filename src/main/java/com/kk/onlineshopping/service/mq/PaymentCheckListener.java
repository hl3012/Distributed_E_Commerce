package com.kk.onlineshopping.service.mq;

import com.alibaba.fastjson.JSON;
import com.kk.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.kk.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.kk.onlineshopping.db.po.OnlineShoppingOrder;
import com.kk.onlineshopping.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.CommonDataSource;

@Component
@Slf4j
@RocketMQMessageListener(topic = "paymentCheck", consumerGroup = "paymentCheckGroup")
public class PaymentCheckListener implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {
    @Resource
    OnlineShoppingCommodityDao onlineShoppingCommodityDao;
    @Resource
    OnlineShoppingOrderDao onlineShoppingOrderDao;
    @Resource
    RocketMQService rocketMQService;
    @Resource
    RedisService redisService;
    @Autowired
    private CommonDataSource commonDataSource;

    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        //check db to see payment status
        //if already payment nothing

        OnlineShoppingOrder orderFromMsg = JSON.parseObject(msg, OnlineShoppingOrder.class);
        OnlineShoppingOrder order = onlineShoppingOrderDao.queryOrderByOrderNum(orderFromMsg.getOrderNo());
        if(order ==null) {
            throw new RuntimeException("Order not found");
        }
        if(order.getOrderStatus()==2) {
            log.info("Order has been paid successfully");
        } else {
            //if no, reverse the commodity stock, update order status to 99
            log.info("Didn't pay the order on time, stock is: {} ", onlineShoppingCommodityDao.queryCommodityById(order.getCommodityId()).getAvailableStock());
            order.setOrderStatus(99);
            onlineShoppingOrderDao.updateOrder(order);

            onlineShoppingCommodityDao.revertStockWithCommodityId(order.getCommodityId());
            String redisKey = "online_shopping:online_shopping_commodity:stock" + order.getCommodityId();
            Long currentStockCount = redisService.revertStockWithCommodityId(redisKey);
            redisService.removeFromDenyList(order.getUserId().toString(), order.getCommodityId().toString());
            log.info("Redis revert commodity:{}, current Stock Count:{}", order.getCommodityId(), currentStockCount);
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
