package com.ly.add.sponge.common.exception;

/**
 * @author : qqy48861
 * date : 2019/5/23.
 */
public class RequestException extends BusiException {
    private Integer code;
    private Exception ex;

    public RequestException(BaseCodeEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public RequestException(BaseCodeEnum exceptionEnum, String param) {
        super(exceptionEnum.getMessage() + "<" + param + ">");
        this.code = exceptionEnum.getCode();
    }

    public RequestException(BaseCodeEnum exceptionEnum, Exception e) {
        super(exceptionEnum.getMessage());
        this.ex = e;
        this.code = exceptionEnum.getCode();
    }

    public RequestException(String contentType, BaseCodeEnum exceptionEnum, Exception e) {
        super(exceptionEnum.getMessage() + "<" + contentType + ">");
        this.ex = e;
        this.code = exceptionEnum.getCode();
    }

    public Integer getCode() {
        return this.code;
    }

    public Exception getEx() {
        return this.ex;
    }
}