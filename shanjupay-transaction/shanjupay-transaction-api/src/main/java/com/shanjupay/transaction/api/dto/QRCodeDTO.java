package com.shanjupay.transaction.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-12
 **/
@Data
@NoArgsConstructor
public class QRCodeDTO implements Serializable {
    private Long merchantId;
    private String appId;
    private Long storeId;//商品标题
    private String subject;
    private String body;//订单描述
}
