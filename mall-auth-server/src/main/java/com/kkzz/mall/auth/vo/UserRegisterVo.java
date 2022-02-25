package com.kkzz.mall.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRegisterVo {
    @NotEmpty(message = "用户名必须提交")
    @Length(min = 6, max = 18, message="用户名长度在6-18字符")
//    @Size(min = 6, max = 18, message = "用户名必须是6-18位字符")
    private String userName;

    @NotEmpty(message = "密码必须提交")
    @Length(min = 6,max = 18,message = "密码必须是6—18位字符")
//    @Size(min = 6, max = 18, message = "密码必须是6-18位字符")
    private String password;

    @NotEmpty(message = "手机号必须提交")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$",message = "手机号格式不正确")
    private String phone;

    @NotEmpty(message = "验证码必须提交")
    private String code;
}
