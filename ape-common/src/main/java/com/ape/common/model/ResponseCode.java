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
    REPEAT_OPERATION(1000, "请勿重复操作"),
    USERNAME_NOT_EXIST(1051, "用户名不存在");

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
