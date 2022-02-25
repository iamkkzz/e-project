package com.kkzz.mall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kkzz.common.utils.HttpUtils;
import com.kkzz.common.utils.R;
import com.kkzz.mall.auth.feign.MemberFeignService;
import com.kkzz.common.vo.MemberRespVo;
import com.kkzz.mall.auth.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理社交登录请求
 */
@Controller
public class OAuth2Controller {

//    private String client_id;
//    private String client_secret;
//    private String grant_type;
//    private String redirect_uri;

    @Autowired
    MemberFeignService memberFeignService;

    @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code") String code, HttpSession session) throws Exception {
        //登录成功,想授权服务器发送请求,获得access_token
        Map<String, String> querys = new HashMap<>();
        querys.put("code", code);
        querys.put("client_id", "678d9204380765bb576754a2ac55a63fdc4e45359d49d70040270ad709cb2393");
        querys.put("client_secret", "fcb1cde172a278ad5600efb90564e54af705ae53dae2fa716354e9dd7bed2078");
        querys.put("grant_type", "authorization_code");
        querys.put("redirect_uri", "http://auth.mall.com/oauth2.0/gitee/success");
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", new HashMap<>(), querys, new HashMap<>());


        //处理获得access_token
        //如果发送请求成功
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            //获取到access_token,先把http里面的响应体转为json
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            String accessToken = socialUser.getAccess_token();
            HashMap<String, String> param = new HashMap<>();
            param.put("access_token", accessToken);
            HttpResponse httpResponse = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<>(), param);
            //带access_token请求第三方,获取uid
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String s = EntityUtils.toString(httpResponse.getEntity());
                Map map = JSON.parseObject(s, Map.class);
                Integer id = (Integer) map.get("id");
                socialUser.setSocial_id(id.toString());
                //判断当前社交用户是否已经注册,若没有,则快速注册
                R r = memberFeignService.oauthLogin(socialUser);
                if (r.getCode() == 0) {
                    MemberRespVo data = r.getData("data", new TypeReference<MemberRespVo>() {
                    });
                    session.setAttribute("loginUser",data);
                    System.out.println("登录成功,用户信息为 + " + data.toString());

                    return "redirect:http://mall.com";
                } else {
                    return "redirect:http://auth.mall.com/login.html";
                }
            }else {
                return "redirect:http://auth.mall.com/login.html";
            }
        } else {
            return "redirect:http://auth.mall.com/login.html";
        }
    }
}
