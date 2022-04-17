package com.kkzz.mall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import com.kkzz.mall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private String app_id = "2021000119630306";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCfD3UawR2k6giMbmAu65J68tUD8HlgKqGX06LLYoeCW0XV4nqRb+Wrsy/OGkN0QziSoESkt50tL/i1AJ4h/wiej8B24fWs5XMFAk4SUw27AnnFrIBZTNcs+eQe+wBKInIXC/H/l3/NT1CJliPim1ahbCZJ4QABphJLnPwSGRW6J9xIsY69eRGaPKsqPD9YJaTaXvKqMqfyWDo2BjegOv4HOEs7d2k6GFC/La/dws+jOs+vQh42CYqnovb+gej3cX5xOjcGx4SZc/Sj3bs4u+QLBXRSx7Q/TMxBOIHLIM7Y57WewRvPabFCXh8TH6XH+Rda+uH3EtSnZ9CAahQOSybDAgMBAAECggEANbA4/cvbXrPkKMqEyrGXIsreXdMIlQ4utxmnZHEgfj6iptwoNPkJE9iKtU2mIRMjgFJ6eQLFGeCMWUQZHC6CUUsCUt5YV4CnPzSU95hzFQFz6uYb+Ih4ROUgO9jLMzrHOcI9b+FKMRmpfJdiC3YVWq0vi1ZX8zaEDGOzrcQr0kZi6Y0f9v2oU8yWWLKHrTXKB+NDZhMXd8oGQAf2pyptgN5WzF9syZtMGnkE3dokZMQo6XB8aXomgKTfAfTe+6snOutP7ISUwD2S0Dbm2Iun5qMgjLgOWPIX8mqXWi17DmDn/3U5J+VkfLI3RdNf8b7x2votbv6nmb30l8cLW0ZgEQKBgQDi6VwTIQ5vYfDUA3aEoTgaMCq7vRxBaRtQnK9UnZ57Ac6DmLozpY4/BevCjpl5anA0YcgyYvmHKwxZ0UgTJLSOm8/EPZ6i0oNBpCQCX//btgAdjf1nadWV9BbrL/rvhW3JULXLIm0vgk96RlGkdJyWxEUWMugX/Swjx7LivQFqKQKBgQCzc2aL8FK1jXjwB4z1+z9wYBm4pv9aXOuaJtMubZau1/UYlB7D8ogstsGjuEa9f2+618np24b7OeP1XNxRHNYnq9E0ToGYYpD7DZfHUaPxzueDjpCn2mzeCriCv/xesm5Jwwqx1r71bhdS2mgfJal3KGkEkCtylVxu5EGft6+/CwKBgQCEU8ihUzbC1+AFFBvqzsYT56EUKP2wW6ZVRXquItlILM6NSeTUt6iTslVB+b0g5GSY96kzUSrpm74xJu4cU9yECfOJItI/a/qlrh5d0DQ6ohHBKovr4flv4I/5CdsSEcLrkwjhdKst+JV23klAWczz03PitT9Kthcmqo/00HphuQKBgCDyDuGiPY0XbBylRjvi5MK84fwErNzqMlcayaq8c+2luFnK19shq96JrwNf0Zz4yIqFE4zbwOxetHtXkRtTL/4YjVANGb3gWr28wSDW3EKIufhtNCGc+F/LmBRpKuw31ge0x0HfKy/kBmQvtO4C/BnkUWOFFHT09wNk5KduEdYBAoGBALus92HflDDJTYZnzi+jfNZO8LhJDy+WftPi2kppQaDX9WVysaak6x09rBcawAoqztUTzxtTSjqRvng5orxo6rbmgcqtq0HFjO0fxKNkFpIMNYFzvdGnIN/uz/GR+ConOknrMKWVVZzals91ZV7hMcxzmzHxxhzaxvCro3FzEZ71";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAghlykUl/p2qKBo3k9m8o41yxSxmIca0sS+NggCmYC1OXzGYTgWjDh+JhI7FJrpNl1UEv4cE6/nmcqF8SUXTg1JcAy67Yldcu+HDGDyizzeLVfKBlgZ3dUVdrabJalIv0ITi9OMRgpMY5vrm62ad0zg2Jh0F7VdSzL1KfBdPJ5ugJvlRSdrG0FJ0+8qLl8uWBBm3fB7TECMXhUF2ggw6k/OPy60WePrLDANwQXpGAzr7LPpyDhOe+ggxR4tiuukz51oNA3KznZnP/QHLWcu9nsIGqsnAai7JSX6Txavo3hscFnLddyj87Glz39H/OlUbUxcTrm8H+h88i+k/x/g5VmwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private String return_url;

    // 签名方式
    private String sign_type = "RSA2";

    private String timeout="30m";

    // 字符编码格式
    private String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应：" + result);

        return result;

    }
}
