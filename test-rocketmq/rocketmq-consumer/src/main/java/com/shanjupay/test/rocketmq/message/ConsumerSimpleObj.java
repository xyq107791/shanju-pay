package com.shanjupay.test.rocketmq.message;

import com.shanjupay.test.rocketmq.model.OrderExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-13
 **/
@Component
@RocketMQMessageListener(topic = "my-topic-obj", consumerGroup = "demo-consumer-group-obj")
public class ConsumerSimpleObj implements RocketMQListener<OrderExt> {
    @Override
    public void onMessage(OrderExt orderExt) {
        System.out.println(orderExt);
    }
}
