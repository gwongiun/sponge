package com.ly.add.sponge.common.param;

import com.ly.add.sponge.common.exception.BusiException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 参数类，可以保存参数，设置参数
 */
public class Params {
    /**
     * 保存具体参数
     */
    private Map<String, Object> params;

    /**
     * 保存参数条件，是否必须
     */
    private Map<String, Boolean> paramsCheck;

    /**
     * 保存参数检查方法
     */
    private Map<String, Function<String, Boolean>> paramsCheckValidation;

    public Params() {
        params = new HashMap<>();
        paramsCheck = new HashMap<>();
        paramsCheckValidation = new HashMap<>();
    }

    /**
     * 设置参数检查，是否是必须的
     *
     * @param paramName
     * @param required
     * @return
     */
    public Params addParamCheck(String paramName, boolean required) {
        paramsCheck.put(paramName, required);
        return this;
    }

    /**
     * 设置参数检查，是否是必须的,参数检查方法
     *
     * @param paramName
     * @param required   是否是必须
     * @param validation 参数检查方法
     * @return
     */
    public Params addParamCheck(String paramName, boolean required, Function<String, Boolean> validation) {
        paramsCheck.put(paramName, required);
        paramsCheckValidation.put(paramName, validation);
        return this;
    }

    public Boolean getParamCheck(String paramName) throws NoSuchParamException {
        if (!paramsCheck.containsKey(paramName)) throw new NoSuchParamException(paramName);
        return paramsCheck.get(paramName);
    }

    public Map<String, Boolean> getParamsCheck() {
        return paramsCheck;
    }

    public Map<String, Function<String, Boolean>> getParamsCheckValidation() {
        return paramsCheckValidation;
    }

    public Integer paramsCheckSize() {
        return paramsCheck.size();
    }


    public Params addParam(String paramName, Object value) {
        params.put(paramName, value);
        return this;
    }

    /**
     * 是否存在某个参数
     *
     * @param paramName
     * @return
     */
    public Boolean containsParam(String paramName) {
        return params.containsKey(paramName);
    }

    /**
     * 获取参数，对不同数据类型的参数可以使用相应的方法
     *
     * @param paramName
     * @return
     */
    public Object getParam(String paramName) {
        if (!params.containsKey(paramName)) {
            return null;
        }
        return params.get(paramName);
    }

    public Integer getIntegerParam(String paramName) {
        try {
            if (!params.containsKey(paramName)) {
                return null;
            }
            return Integer.parseInt(params.get(paramName).toString());
        } catch (NumberFormatException e) {
            throw new ParamParseException(paramName);
        }
    }

    public Long getLongParam(String paramName) {
        try {
            if (!params.containsKey(paramName)) {
                return null;
            }
            return Long.parseLong(params.get(paramName).toString());
        } catch (NumberFormatException e) {
            throw new ParamParseException(paramName);
        }
    }

    public String getStringParam(String paramName) {
        if (!params.containsKey(paramName) || params.get(paramName) == null) {
            return null;
        }
        return params.get(paramName).toString();
    }

    public String getStringParam(String paramName, String defaultParamName) {
        if (params.containsKey(paramName) || params.get(paramName) == null) {
            return params.get(paramName).toString();
        } else{
            return params.getOrDefault(defaultParamName, "").toString();
        }
    }

    public Boolean getBooleanParam(String paramName) {
        if (!params.containsKey(paramName) || params.get(paramName) == null) {
            return false;
        }
        Object paramValue = params.get(paramName);
        if (paramValue instanceof Boolean) {
            return (Boolean) paramValue;
        } else {
            String value = paramValue.toString();
            switch (value) {
                case ("true"):
                    return true;
                case ("false"):
                    return false;
                case ("1"):
                    return true;
                case ("0"):
                    return false;
                default:
                    throw new ParamParseException(paramName);
            }
        }
    }

    public Double getDoubleParam(String paramName) {
        if (!params.containsKey(paramName) || params.get(paramName) == null) {
            return null;
        }
        Object paramValue = params.get(paramName);
        if (paramValue instanceof Double) {
            return (Double) paramValue;
        } else {
            try {
                return Double.parseDouble(paramValue.toString());
            } catch (NumberFormatException e) {
                throw new ParamParseException(paramName);
            }
        }
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Integer paramsSize() {
        return params.size();
    }


    public class NoSuchParamException extends BusiException {
        public NoSuchParamException(String paramName) {
            super("没有该名称的参数[" + paramName + "]", 405);
        }
    }

    public class ParamParseException extends BusiException {
        public ParamParseException(String paramName) {
            super("转换获取参数错误[" + paramName + "]", 405);
        }
    }

}
