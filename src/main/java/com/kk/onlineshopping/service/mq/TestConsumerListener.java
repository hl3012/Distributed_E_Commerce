package com.kk.onlineshopping.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "consumerTopic", consumerGroup = "consumerGroup")
public class TestConsumerListener implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        log.info("this is " + messageExt.getReconsumeTimes() + " times");
        log.info("Received message " + msg);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setMaxReconsumeTimes(2);
        consumer.setConsumeTimeout(5000);
        consumer.setConsumeThreadMin(1);
        consumer.setConsumeThreadMax(1);
    }
}
