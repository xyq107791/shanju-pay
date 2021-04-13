package com.shanjupay.test.rocketmq.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-13
 **/
@Data
@NoArgsConstructor
@ToString
public class OrderExt implements Serializable {
    private String id;
    private Date createTime;
    private Long money;
    private String title;
}
