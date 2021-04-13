package com.shanjupay.test.rocketmq.message;

import com.alibaba.fastjson.JSON;
import com.shanjupay.test.rocketmq.model.OrderExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-13
 **/
@Component
@RocketMQMessageListener(topic = "my-topic", consumerGroup = "demo-consumer-group")
public class ConsumerSimple implements RocketMQListener<String> {

    //接收到消息调用此方法
    @Override
    public void onMessage(String s) {
        OrderExt orderExt = JSON.parseObject(s, OrderExt.class);
        System.out.println(orderExt);
//        System.out.println(s);
    }
}
