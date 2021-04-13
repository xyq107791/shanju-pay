package com.shanjupay.paymentagent.message;

import com.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-13
 **/
@Component
@Slf4j
public class PayProducer {
    //消息Topic
    private static final String TOPIC_ORDER = "TP_PAYMENT_ORDER";

    private static final String TOPIC_RESULT = "TP_PAYMENT_RESULT";

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void payResultNotice(PaymentResponseDTO result) {
        rocketMQTemplate.convertAndSend(TOPIC_RESULT, result);
    }

    public void payOrderNotice(PaymentResponseDTO result) {
        log.info("支付通知发送延迟消息:{}", result);

        try {
            Message<PaymentResponseDTO> message = MessageBuilder.withPayload(result).build();
            SendResult sendResult = rocketMQTemplate.syncSend(TOPIC_ORDER, message, 1000, 3);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

    }
}