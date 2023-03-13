package com.wrc.cloud.entity;

public enum ResponseCodeEnum {
    /**
     *
     */
    SUCCESS(20000, "成功"),
    FAIL(500, "失败"),
    LOGIN_ERROR(1000, "用户名或密码错误"),
    LOGIN_EMPTY_ERROR(1001, "用户名或者密码不能为空"),

    UNKNOWN_ERROR(2000, "未知错误"),
    PARAMETER_ILLEGAL(2001, "参数不合法"),

    TOKEN_INVALID(2002, "token 已过期或验证不正确！"),
    TOKEN_SIGNATURE_INVALID(2003, "无效的签名"),
    TOKEN_EXPIRED(2004, "token 已过期"),
    TOKEN_MISSION(2005, "token 缺失"),
    TOKEN_CHECK_INFO_FAILED(2006, "token 信息验证失败"),
    REFRESH_TOKEN_INVALID(2006, "refreshToken 无效"),
    LOGOUT_ERROR(2007, "用户登出失败");
    private final int code;
    private final String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
