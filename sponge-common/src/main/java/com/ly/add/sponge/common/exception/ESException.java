package com.ly.add.sponge.common.exception;

import java.util.Map;

/**
 * @author : qqy48861
 * date : 2019/5/23.
 */
public class ESException extends BusiException {
    private Integer code;
    private Exception ex;
    private Map<String, Object> logMap;

    public ESException(BaseCodeEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public ESException(BaseCodeEnum exceptionEnum, String index) {
        super(exceptionEnum.getMessage() + "<" + index + ">");
        this.code = exceptionEnum.getCode();
    }

    public ESException(BaseCodeEnum exceptionEnum, String index, Map<String, Object> logMap) {
        super(exceptionEnum.getMessage() + "<" + index + ">");
        this.code = exceptionEnum.getCode();
        this.logMap = logMap;
    }

    public ESException(BaseCodeEnum exceptionEnum, String index, Map<String, Object> logMap, Exception e) {
        super(exceptionEnum.getMessage() + "<" + index + ">");
        this.ex = e;
        this.code = exceptionEnum.getCode();
        this.logMap = logMap;
    }

    public Integer getCode() {
        return this.code;
    }

    public Exception getEx() {
        return this.ex;
    }

    public Map<String, Object> getLogMap() {
        return this.logMap;
    }
}