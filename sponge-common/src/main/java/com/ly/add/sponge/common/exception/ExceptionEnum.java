package com.ly.add.sponge.common.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author : qqy48861
 * date : 2019/5/23.
 */
public enum ExceptionEnum implements BaseCodeEnum {
    REQUEST_SUCCESS(200,"操作成功"),
    REQUEST_JSON_MALFORMED_ERROR(450, "请传入正确的JSON"),
    REQUEST_TOKEN_NULL_ERROR(451, "TOKEN为空"),
    REQUEST_TOKEN_INVALID_ERROR(452, "无效的TOKEN"),
    REQUEST_METHOD_NOT_SUPPORT_ERROR(460, "不支持的请求方式"),
    REQUEST_HEADER_MEDIATYPE_ERROR(461, "不支持的Content-Type"),
    REQUEST_PARAM_MISSING(471, "缺少请求参数"),
    REQUEST_PARAM_ILLEGAL(472, "不合法的请求参数"),
    UNKNOWN_ERROR(555, "服务器开小差了，稍后重试哦"),
    CC_READ_ERROR(570, "配置中心读取异常"),
    CC_LOSE_ERROR(571, "配置中心配置丢失异常"),
    ES_INDEX_NOT_FOUND_ERROR(560, "查询ES索引不存在"),
    ES_TIMEOUT_ERROR(561, "查询ES超时");

    private Integer code;
    private String message;

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getCodes() {
        return (String) Arrays.stream(values()).map((item) -> {
            return item.getCode().toString();
        }).collect(Collectors.joining(","));
    }
}