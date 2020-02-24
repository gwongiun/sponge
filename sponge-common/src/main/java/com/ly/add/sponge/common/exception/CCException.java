package com.ly.add.sponge.common.exception;

/**
 * @author : qqy48861
 * date : 2019/5/23.
 */
public class CCException extends BusiException {
    private Integer code;
    private Exception ex;

    public CCException(BaseCodeEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public CCException(BaseCodeEnum exceptionEnum, Exception e) {
        super(exceptionEnum.getMessage());
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