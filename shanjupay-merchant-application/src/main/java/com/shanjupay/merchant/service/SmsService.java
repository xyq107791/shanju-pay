package com.shanjupay.merchant.service;

public interface SmsService {

    /**
     * 获取短信验证码
     */
    String sendMsg(String phone);
}
