package com.ly.add.sponge.common.exception;

/**
 * @author : qqy48861
 * date : 2019/5/23.
 */
public class UnknownException extends BusiException {
    private Integer code;
    private Exception ex;

    public UnknownException(BaseCodeEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public UnknownException(Exception e) {
        super(ExceptionEnum.UNKNOWN_ERROR.getMessage());
        this.code = ExceptionEnum.UNKNOWN_ERROR.getCode();
        this.ex = e;
    }

    public UnknownException(BaseCodeEnum exceptionEnum, Exception e) {
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