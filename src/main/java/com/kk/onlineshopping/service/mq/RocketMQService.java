package com.kk.onlineshopping.service.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RocketMQService {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendFIFOMessage(String topic, String messageBody) {
        Message msg = new Message(topic, messageBody.getBytes());
        try {
            rocketMQTemplate.getProducer().send(msg, (mqs, message, arg) -> mqs.get(0), null);
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        } catch (RemotingException e) {
            throw new RuntimeException(e);
        } catch (MQBrokerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String topic, String messageBody) {
        Message message = new Message(topic, messageBody.getBytes());
        try {
            rocketMQTemplate.getProducer().send(message);
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        } catch (RemotingException e) {
            throw new RuntimeException(e);
        } catch (MQBrokerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDelayMessage(String topic, String messageBody, int delayTimeLevel) {
        Message message = new Message(topic, messageBody.getBytes());
        message.setDelayTimeLevel(delayTimeLevel);
        try {
            rocketMQTemplate.getProducer().send(message);
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        } catch (RemotingException e) {
            throw new RuntimeException(e);
        } catch (MQBrokerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
