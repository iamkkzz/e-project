package com.kkzz.common.exception;

public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常"),
    VAILD_EXCEPTION(10001,"参数格式校验失败"),
    USER_EXIST_EXCEPTION(15001,"用户已经存在"),
    PHONE_EXIST_EXCEPTION(15002,"手机号已经存在"),
    LOGINACCT_PASSWORD_EXCEPTION(15003,"账号密码错误"),
    SOCIAL_LOGIN_EXCEPTION(15004,"关联新用户失败"),
    SMS_CODE_EXCEPTION(10002,"验证码获取频率过高,稍后再试");

    private int code;
    private String msg;
    BizCodeEnume(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
