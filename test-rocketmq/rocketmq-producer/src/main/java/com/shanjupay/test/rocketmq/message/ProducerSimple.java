package com.shanjupay.test.rocketmq.message;

import com.shanjupay.test.rocketmq.model.OrderExt;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-13
 **/
@Component
public class ProducerSimple {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 消息内容为json格式
     * @param topic
     * @param orderExt
     */
    public void sendAsyncMsgByJsonDelay(String topic, OrderExt orderExt) throws Exception {
        //消息内容将orderExt转为json
        String json = this.rocketMQTemplate.getObjectMapper().writeValueAsString(orderExt);
        org.apache.rocketmq.common.message.Message message = new
                org.apache.rocketmq.common.message.Message(topic, json.getBytes(Charset.forName("utf-8")));
        //设置延迟等级
        message.setDelayTimeLevel(3);
        //发送异步消息
        this.rocketMQTemplate.getProducer().send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
        System.out.printf("send msg : %s", orderExt);
    }

    /**
     * 发送延迟消息
     * @param topic
     * @param orderExt
     */
    public void sendMsgByJsonDelay(String topic, OrderExt orderExt) {
        //发送同步消息，消息内容将orderExt转为json
        Message<OrderExt> message = MessageBuilder.withPayload(orderExt).build();
        //指定发送超时时间(毫秒)和延迟等级
        this.rocketMQTemplate.syncSend(topic, message, 1000, 3);

        System.out.printf("send msg : %s", orderExt);
    }

    public void sendMsgByJson(String topic, OrderExt orderExt) {
        //发送同步消息，消息内容将orderExt转为json
        this.rocketMQTemplate.convertAndSend(topic, orderExt);
        System.out.printf("send msg : %s", orderExt);
    }

    public void sendASyncMsg(String topic, String msg) {
        rocketMQTemplate.asyncSend(topic, msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                //成功回调
                System.out.println("successMsg:" + sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable throwable) {
                //异常回调
                System.out.println("errorMsg:" + throwable.getMessage());
            }
        });
    }

    public void sendSyncMsg(String topic, String msg) {
        rocketMQTemplate.syncSend(topic, msg);
    }
}