package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-06
 **/
@Service
public class FileServiceImpl implements FileService {
    @Value("${oss.qiniu.url}")
    private String qiniuUrl;
    @Value("${oss.qiniu.accessKey}")
    private String accessKey;
    @Value("${oss.qiniu.secretKey}")
    private String secretKey;
    @Value("${oss.qiniu.bucket}")
    private String bucket;

    @Override
    public String upload(byte[] bytes, String fileName) {
        try {
            QiniuUtils.upload2qiniu(accessKey, secretKey, bucket, bytes, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100106);
        }
        //返回文件名称 return qiniuUrl+fileName;
        return qiniuUrl + fileName;
    }
}
