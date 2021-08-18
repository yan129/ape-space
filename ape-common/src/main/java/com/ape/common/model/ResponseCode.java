package com.ape.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/13
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCode {

    SUCCESS(200, "操作成功"),
    ERROR(400, "操作失败"),
    UNAUTHORIZED(401, "请先登录"),
    FORBIDDEN(403, "权限不足"),

    REPEAT_SUBMIT(1000, "请勿重复提交"),
    REPEAT_SEND(1001, "请勿重复发送"),
    TOKEN_MISSING(1002, "Token缺失"),

    REGISTER_CHECK_CODE(1501, "验证码错误"),
    REGISTER_CODE_EXPIRED(1502, "验证码过期，请新获取"),


    USER_LOGIN_FAILURE(2001, "登录失败"),
    USERNAME_NOT_EXIST(2002, "账号不存在"),
    USER_ACCOUNT_EXPIRED(2003, "账号过期，请联系客服"),
    USER_CREDENTIALS_ERROR(2004, "违规封禁"),
    USER_CREDENTIALS_EXPIRED(2005, "密码过期，请重新设置"),
    USER_ACCOUNT_DISABLE(2006, "账户违规禁用，请联系客服"),
    USER_ACCOUNT_LOCKED(2007, "账户被锁定，请联系客服"),
    USER_ACCOUNT_ALREADY_EXIST(2009, "账号已存在"),
    USER_ACCOUNT_LOGOUT_SUCCESS(2010, "账号注销成功");

    private Integer code;
    private String msg;

    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
