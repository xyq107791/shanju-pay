package com.shanjupay.transaction.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class PayTestController {

    String APP_ID = "2021000117639421";
    //应用私钥
    String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCP0cLaxsol6w9hQkx/qH79N0I81FwJtU1JZienPtzD7GnvLzKhlS1UesyzbiiSBTm7lLr6/MbNZY+wZj5le36xYt1wRFxZ++xnBbJdgJk34L0XDXH0vC99OjfRLh116jXB0GX06iQvylHyO8qEq1of8ROc0/kL+DcQmEIAaYIrxqzzmazD81/VenWRiNUio97+oRN51xOf2BOy4Dd3bm/0nXaf25bEpX7Y86OTXKIM4k75iRoopAqWn6j4tRCcHt5sl3o0zkFhB030QPsyZSuU0cg0N1dXgmrd0SsOpVepNwZdJ9duMZ8uvYoQyAkPDOGQvqBIuZtpUp1toZSaR6dRAgMBAAECggEBAIs9UGOztp8Ddv54y8uEfH33c/+ksf8MMulvJ9D6QY7GYsGZdnGOFjcGNQ07B7hnqr97fZPmCytsurLUKnDiIYTn7O/2n1rXVsdfcpKoIK9CvI+HCt8hGvEo3kV9WjSBLs6YTz3ROnuya6gUTzwnEbh/FLfvWd1G0TkV0EDJ5acu28ToW/KFsDIWTZj779pLmk+teBUFphVp4tKcAWqLeRvnSiQsigCjDGkbszHnHQsnDrI0X9RoBVqvJl5C3JxGvoD10mcHhoOyuiHiTzhifyIqW4Fqt0k7QOF291soKkQRxmxwsJopPcJe1ciZE8w0TzlU3OoFuv04mIAITwxuvEkCgYEA0kjTvPeK84FfvejynMWTav6/rfOEHyv7BTqVVnUl6vegz+4ctKkk+idmyUjyav5s05Q59t5ihNPtQOC6Gk0C4du9uAjNzhljWr7tNJFz/xd8eqpE3O6ftqnjZd+ZprNHDHQ7Q4W88RaRYy7m+qryyPh6Cl+IFXMlXAQRLrsHRAMCgYEArxXimojBh7FNpPLXwYqeMFEXW3XSxNTCpQSGvLwoNhKSIKYEpFsmFsnWp1m6seUiqKrxehCNeu/LYILH/h2VEGCUFx9djmdvzGywcqgA/+2QULonDiCbM3Qa28Lk53v8NyWyrQ6HD/YFRe1qMXJ5Fzl1+BAX2cIIypCC8zY1KRsCgYEAwyVosh37qjzur9wFgm1sgEfHdFPf0dnLyy9xtVlkBekmKLCtnQ63TRrRrYxvw9E3ByawoCQCw4e0zpd+vbeoLBfzuV0aOOCgrm8uzLELN7kBAQkQfQNJ3odMi5b1DZmkHq+i6+epzwaFh8jX+9o3E0BcaVH8hQNlsT3pADf65ycCgYB4Ms4eyUQTo+VqGE2G0WNISGOApqLL/kSihTj62THB7X3kbBVDwE+l1WtH+JHzDxt8L98XrcVzYKx+1Un3aterJqKEPUQ9P0XBUdzQ2gXVrYIl6aZmTHxAFrVZqbL8OGIuefLwW8jdbEiyeQsuZyg6iXgzlNjK5iqi5hMAZXaZgQKBgCpTbED573M68YmpZsNoiieUOmcCu8ttY5W/7mZOCI8j+UnctT8GhSzgHaWOj8TM0D6NdM7LR1KhGixzi6l33APO81gVt9e00cOeVR4PoSrxo+BklHDx1c8y/EfATxfEfiqEnQp9b07sGA6lpBVpm9uT0M80KBQAuZ/TlAcs0s8Z";
    //支付宝公钥
    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtREq/CXQQ29c5NfV712ZDK7zwdnG56UVxd8KHRQ8T0fFomZct0wUis75h9EcBIJEuDzsdJrypQ4/eFRfYsdsdiAK5vwR1z0tbDRnjLOPb/fIOxLfOXbjH9YjKU/xuC+F8t7oIaPTO9BAn7nMrfpt8VLP2NuEyUXTO+0YjG9VdQ1NrzkMMMgG5jKmqMN0PamTlOQMqivxqGY2uA45CrSvXiZ2I8MOVJkXNE++cTyYNCrZWGpKLQlcolGFcRaZeJpwMIUsgKEanLFI4sKXcDIjT3NXLn0liN/CdU/rLL1V16Il7+djF2o9pQKqJOzGCPXGe98hxIYRKnxrjL88M86DJQIDAQAB";

    String CHARSET = "utf-8";

    String serverUrl = "https://openapi.alipaydev.com/gateway.do";

    //签名算法类型
    String sign_type = "RSA2";

    @GetMapping("/alipaytest")
    public void alipaytest(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws IOException {
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, sign_type); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\"20150420010101217\"," +
                " \"total_amount\":\"88.88\"," +
                " \"subject\":\"Iphone6 16G\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
                " }");//填充业务参数
        String form="";
        try {
            //请求支付宝下单接口,发起http请求
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }
}
