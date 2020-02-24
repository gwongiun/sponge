package com.ly.add.sponge.common.exception;


import com.ly.add.sponge.common.constant.RspConstants;

/**
 * 基础的自定义可以处理的运行时业务异常<br>
 * 日志中打印Info
 *
 * @author mkk41112
 * @since 2016-9-27
 */
public class BusiException extends RuntimeException {
    private Integer code;
    private Throwable ex;

    public BusiException(String message) {
        super(message);
        this.code = RspConstants.RSP_CODE_SERVER_ERROR;
    }

    public BusiException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusiException(String message, Integer code, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.ex = cause;
    }

    public BusiException(String message, Throwable cause) {
        super(message, cause);
        this.code = RspConstants.RSP_CODE_SERVER_ERROR;
        this.ex = cause;
    }

    public BusiException(Throwable cause) {
        super(cause.getMessage(), cause);
        this.code = RspConstants.RSP_CODE_SERVER_ERROR;
        this.ex = cause;
    }

    public Integer getCode() {
        return code;
    }

    public Throwable getEx() {
        return ex;
    }
}
