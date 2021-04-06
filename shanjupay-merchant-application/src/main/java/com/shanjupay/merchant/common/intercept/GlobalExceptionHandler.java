package com.shanjupay.merchant.common.intercept;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.domain.ErrorCode;
import com.shanjupay.common.domain.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-06
 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse processException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        if (e instanceof BusinessException) {
            log.info(e.getMessage(), e);
            BusinessException businessException = (BusinessException) e;
            ErrorCode errorCode = businessException.getErrorCode();
            return new RestErrorResponse(errorCode.getDesc(), String.valueOf(errorCode.getCode()));
        }
        log.error("系统异常：", e);
        return new RestErrorResponse(CommonErrorCode.UNKNOWN.getDesc(), String.valueOf(CommonErrorCode.UNKNOWN.getCode()));
    }
}
