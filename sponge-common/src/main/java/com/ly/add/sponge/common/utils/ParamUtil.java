package com.ly.add.sponge.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ly.add.sponge.common.exception.BusiException;
import com.ly.add.sponge.common.exception.ExceptionEnum;
import com.ly.add.sponge.common.exception.RequestException;
import com.ly.add.sponge.common.param.Params;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author mkk41112
 * @since 2017/2/6
 */
public class ParamUtil {

    /**
     * 从GET请求中获取请求参数<br>
     * <strong>注意：</strong>多个相同参数名时，只取到第一个<br>
     *
     * @param paramsCheck    需要获取的参数设置
     * @param request
     * @return
     */
    public static Params getGetParams(Params paramsCheck, HttpServletRequest request) throws BusiException {
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            params.put(entry.getKey(), entry.getValue()[0]);
        }
        JSONObject paramsJson = JSONObject.parseObject(JSON.toJSONString(params));
        return processParams(paramsCheck, paramsJson);
    }

    /**
     * 从Attribute请求中获取请求参数<br>
     * <strong>注意：</strong>多个相同参数名时，只取到第一个<br>
     * @param paramsCheck    需要获取的参数设置
     * @param request
     * @return
     */
    public static Params getAttributeParams(Params paramsCheck, HttpServletRequest request) throws BusiException {
        JSONObject paramsJson = (JSONObject)request.getAttribute("params");
        return processParams(paramsCheck, paramsJson);
    }

    /**
     * 从GET请求中获取请求参数<br>
     * <strong>注意：</strong>多个相同参数名时，只取到第一个<br>
     * 默认解码memberId
     *
     * @param paramsCheck 需要获取的参数设置
     * @param request
     * @return
     */

    /**
     * 从POST请求中获取请求参数<br>
     *
     * @param request
     * @return
     */
    public static Params getPostParams(Params paramsCheck, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            request.setCharacterEncoding("UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        JSONObject paramsJson = JSONObject.parseObject(sb.toString());
        return processParams(paramsCheck, paramsJson);
    }

    /**
     * 从POST请求中获取请求参数<br>
     * 如果参数中有 memberId，进行解码后保存
     *
     * @param paramsCheck 需要获取的参数设置
     * @param request
     * @return
     */
    public static Params getPostParamsDecodeMemberId(Params paramsCheck, HttpServletRequest request) {
        return getPostParams(paramsCheck, request);
    }

    /**
     * 从ParamJson 中获取参数
     *
     * @param params
     * @param paramsJson
     * @return
     */
    private static Params processParams(Params params, JSONObject paramsJson) {
        params = params == null ? new Params() : params;
        if (null != paramsJson) {
            for (String paramName : paramsJson.keySet()) {
                String value = paramsJson.getString(paramName);
                params.addParam(paramName, StringUtils.isNotEmpty(value) ? value.trim() : "");
            }
        }
        if (params.paramsCheckSize() > 0) {
            Map<String, Function<String, Boolean>> validations = params.getParamsCheckValidation();
            for (Map.Entry<String, Boolean> entry : params.getParamsCheck().entrySet()) {
                String paramValue = params.getStringParam(entry.getKey());
                if (StringUtils.isEmpty(paramValue) && entry.getValue()) {
                    throw new RequestException(ExceptionEnum.REQUEST_PARAM_MISSING, entry.getKey());
                }
                Function<String, Boolean> validation = validations.get(entry.getKey());
                if (validation != null && !validation.apply(paramValue)) {
                    throw new RequestException(ExceptionEnum.REQUEST_PARAM_ILLEGAL, entry.getKey());
                }
            }
        }
        return params;
    }
}
