package com.ly.add.sponge.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.ly.add.sponge.common.constant.RspConstants;
import com.ly.add.sponge.common.exception.BaseCodeEnum;
import com.ly.add.sponge.common.exception.BusiException;
import com.ly.add.sponge.common.exception.ExceptionEnum;

/**
 * Json返回数据工具类
 *
 * @author : qqy48861
 */
public class JsonUtil {

    private static final String RESP_CODE = "code";
    private static final String RESP_SUCCESS = "success";
    private static final String RESP_MESSAGE = "message";
    private static final String RESP_DATA = "data";
    private static final String RESP_LOG = "log";
    private static final String RESP_TRACE_ID = "trace-id";

    private static final String RSP_STRING_SUCCESS = "操作成功";
    private static final String RSP_STRING_FAILED = "操作失败";
    public static final String RSP_STRING_ERROR_WAIT = "系统繁忙,请稍候哦~";

    @Deprecated
    public static JSONObject getSuccessResponse(String message) {
        return getSuccessResponse(message, RspConstants.RSP_CODE_SUCCESS, null);
    }

    @Deprecated
    public static JSONObject getSuccessResponse(String message, Integer succCode) {
        return getSuccessResponse(message, succCode, null);
    }

    @Deprecated
    public static JSONObject getSuccessResponse(String message, Object data) {
        return getSuccessResponse(message, RspConstants.RSP_CODE_SUCCESS, data);
    }

    @Deprecated
    public static JSONObject getSuccessResponse(Object data) {
        return getSuccessResponse(RSP_STRING_SUCCESS, RspConstants.RSP_CODE_SUCCESS, data);
    }

    /**
     * 成功时返回Json
     *
     * @param message  成功信息
     * @param succCode 成功代码
     * @param data     返回数据
     * @return
     */
    @Deprecated
    public static JSONObject getSuccessResponse(String message, Integer succCode, Object data) {
        JSONObject rspJson = new JSONObject();
        rspJson.put(RESP_CODE, succCode);
        rspJson.put(RESP_SUCCESS, true);
        rspJson.put(RESP_MESSAGE, message);
        rspJson.put(RESP_DATA, data);
        return rspJson;
    }

    @Deprecated
    public static JSONObject getErrorResponse(Object data) {
        return getErrorResponse(RSP_STRING_FAILED, RspConstants.RSP_CODE_ERROR, false, data);
    }

    @Deprecated
    public static JSONObject getErrorResponse(String message, Object data) {
        return getErrorResponse(message, RspConstants.RSP_CODE_ERROR, false, data);
    }

    @Deprecated
    private static JSONObject getErrorResponse(String message, Integer failCode, boolean isSuccess, Object data) {
        JSONObject rspJson = new JSONObject();
        rspJson.put(RESP_CODE, failCode);
        rspJson.put(RESP_SUCCESS, isSuccess);
        rspJson.put(RESP_MESSAGE, message);
        rspJson.put(RESP_DATA, data);
        return rspJson;
    }

    @Deprecated
    public static JSONObject getErrorResponse(String message) {
        return getErrorResponse(message, RspConstants.RSP_CODE_ERROR, false);
    }

    @Deprecated
    public static JSONObject getErrorResponse(String message, Integer errCode) {
        return getErrorResponse(message, errCode, false);
    }

    @Deprecated
    public static JSONObject getErrorResponse(Exception e) {
        if (e instanceof BusiException) {
            return getErrorResponse(e.getMessage(), ((BusiException) e).getCode(), false);
        } else {
            return getErrorResponse(RSP_STRING_ERROR_WAIT, RspConstants.RSP_CODE_ERROR, false);
        }
    }

    /**
     * 错误或异常时返回Json
     *
     * @param message   错误或异常信息
     * @param errCode   错误代码
     * @param isSuccess 错误类型
     * @return
     */
    @Deprecated
    public static JSONObject getErrorResponse(String message, Integer errCode, boolean isSuccess) {
        JSONObject rspJson = new JSONObject();
        rspJson.put(RESP_CODE, errCode);
        rspJson.put(RESP_SUCCESS, isSuccess);
        rspJson.put(RESP_MESSAGE, message);
        rspJson.put(RESP_DATA, null);
        return rspJson;
    }

    @Deprecated
    public static JSONObject getSuccessWithLog(Object data, Object logMap) {
        return getSuccessResponse(RSP_STRING_SUCCESS, RspConstants.RSP_CODE_SUCCESS, data, logMap);
    }

    @Deprecated
    public static JSONObject getSuccessResponse(String message, Integer succCode, Object data, Object logMap) {
        JSONObject rspJson = new JSONObject();
        rspJson.put(RESP_CODE, succCode);
        rspJson.put(RESP_SUCCESS, true);
        rspJson.put(RESP_MESSAGE, message);
        rspJson.put(RESP_DATA, data);
        rspJson.put(RESP_LOG, logMap);
        return rspJson;
    }


    /**
     * the new JsonUtil
     */
    public static JSONObject success(Integer code, String message, Object data) {
        JSONObject success = new JSONObject();
        success.put(RESP_CODE, code);
        success.put(RESP_SUCCESS, true);
        success.put(RESP_MESSAGE, message);
        success.put(RESP_DATA, data);
        success.put(RESP_TRACE_ID, UUIDUtil.getUUID());
        return success;
    }

    public static JSONObject success(BaseCodeEnum codeEnum, Object data) {
        return success(codeEnum.getCode(), codeEnum.getMessage(), data);
    }

    public static JSONObject success(Object data) {
        return success(ExceptionEnum.REQUEST_SUCCESS, data);
    }

    public static JSONObject success(BaseCodeEnum codeEnum, Object data, Object log) {
        JSONObject success = success(codeEnum, data);
        success.put(RESP_LOG, log);
        return success;
    }

    public static JSONObject failure(Integer code, String message, Object data) {
        JSONObject failure = new JSONObject();
        failure.put(RESP_CODE, code);
        failure.put(RESP_SUCCESS, false);
        failure.put(RESP_MESSAGE, message);
        failure.put(RESP_DATA, data);
        failure.put(RESP_TRACE_ID, UUIDUtil.getUUID());
        return failure;
    }

    public static JSONObject failure(BaseCodeEnum codeEnum, Object data) {
        return failure(codeEnum.getCode(), codeEnum.getMessage(), data);
    }

    public static JSONObject failure(BaseCodeEnum codeEnum, Object data, Object log) {
        JSONObject success = failure(codeEnum, data);
        success.put(RESP_LOG, log);
        return success;
    }

    public static JSONObject failure(Exception e) {
        if (e instanceof BusiException) {
            return failure(((BusiException) e).getCode(), e.getMessage(), null);
        } else {
            return failure(ExceptionEnum.UNKNOWN_ERROR, null);
        }
    }


}
