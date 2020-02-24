package com.ly.add.sponge.common.http;

import com.ly.add.sponge.common.exception.BaseCodeEnum;

/**
 * @author : qqy48861
 * date : 2019/3/27.
 */
public enum HttpCodeEnum implements BaseCodeEnum {

    HTTP_REQUEST_SUCCESS(2000, "HTTP请求成功", true),
    HTTP_REQUEST_ERROR(2100, "HTTP请求错误", false),
    HTTP_RESPONSE_ERROR(2200, "HTTP响应错误", false),
    HTTP_RESPONSE_NULL_ERROR(2201, "HTTP响应为空", false),
    HTTP_RESPONSE_EMPTY_ERROR(2202, "HTTP响应为空字符", false),
    HTTP_RESPONSE_JSON_ERROR(2203, "HTTP响应非JSON", false);

    private Integer code;
    private String message;
    private Boolean success;

    HttpCodeEnum(Integer code, String message, Boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}