package com.ycj.idempotent.enums;

/**
 * @author jackspeed
 * @date 2020/03/27
 */
public enum ResultCode {
    NEED_TOKEN("需要token", 201),
    TOKEN_ERROR("token校验失败", 302);

    private String msg;
    private Integer code;

    ResultCode(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
