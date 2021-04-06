package com.shanjupay.merchant.service;

public interface SmsService {

    /**
     * 校验验证码，抛出异常则校验无效
     * @param verifiyKey 验证码key
     * @param verifiyCode 验证码
     */
    void checkVerifiyCode(String verifiyKey, String verifiyCode);

    /**
     * 获取短信验证码
     */
    String sendMsg(String phone);
}
