package com.shanjupay.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class MerchantApplicationBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(MerchantApplicationBootstrap.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        // 消息转换器列表
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        // 配置消息转换器StringHttpMessageConverter,并设置utf-8
        messageConverters.set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文字符集
        return restTemplate;
    }
}
