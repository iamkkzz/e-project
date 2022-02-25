package com.kkzz.mall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.kkzz.common.constant.AuthServerConstant;
import com.kkzz.common.exception.BizCodeEnume;
import com.kkzz.common.utils.R;
import com.kkzz.common.vo.MemberRespVo;
import com.kkzz.mall.auth.feign.MemberFeignService;
import com.kkzz.mall.auth.feign.ThirdPartFeignService;
import com.kkzz.mall.auth.vo.UserLoginVo;
import com.kkzz.mall.auth.vo.UserRegisterVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {
    @Autowired
    ThirdPartFeignService thirdPartFeignService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignService;

    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone) {
        //1.接口防刷
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (StringUtils.isNotEmpty(redisCode)) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000) {
                //60s内不能再发
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(), BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            } else {

            }
        }
        //2.验证码再次校验
        String code = UUID.randomUUID().toString().substring(0, 5);
        String subString = code + "_" + System.currentTimeMillis();
        //redis缓存验证码,防止同一个phone在60s内再次发送验证码
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, subString, 10, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(phone, code);
        return R.ok();
    }

    /**
     * todo 分布式下的session问题
     *
     * @param vo
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo vo, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                if (StringUtils.isEmpty(errors.get(fieldError.getField()))) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                } else {
                    if (fieldError.getDefaultMessage().contains("必须提交")) {
                        errors.replace(fieldError.getField(), fieldError.getDefaultMessage());
                    }
                }
            }
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/register.html";
        }
        //校验验证码,从redis中取出校验码判断是否符合
        String code = vo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (StringUtils.isNotEmpty(s)) {
            if (code.equals(s.split("_")[0])) {
                //验证码校验成功,实现真正注册,调用远程服务
                //先删除验证码;令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                R r = memberFeignService.register(vo);
                if (r.getCode() == 0) {
                    //注册成功
                    return "redirect:http://auth.mall.com/login.html";
                } else {
                    //注册失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getData("msg",new TypeReference<String>() {
                    }));
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.mall.com/register.html";
                }
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.mall.com/register.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/register.html";
        }
    }


    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes,HttpSession session){
        //远程登录
        R r = memberFeignService.login(vo);
        if (r.getCode()==0){
            //登录成功 将数据放入session
            MemberRespVo data = r.getData("data", new TypeReference<MemberRespVo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER,data);
            return "redirect:http://mall.com";
        }else {
            //登录失败
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", r.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/login.html";
        }
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session){
        if(session.getAttribute(AuthServerConstant.LOGIN_USER)==null){
            //没有登录
            return "login";
        }
        //登录成功
        return "redirect:http://mall.com/login.html";
    }

}
