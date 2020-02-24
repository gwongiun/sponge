package com.ly.add.sponge.tcel.log;

import com.ly.tcbase.skynet.Logger;
import com.ly.tcbase.skynet.Marker;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 天网日志工厂
 * Created by zh32320 on 2016/12/2.
 */
public class LogFactory {

    private static String env;

    public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS = ParameterizedMessageFactory.class;
    private MessageFactory messageFactory;

    private Marker marker;
    private String message;
    private Exception exception;
    private String filter1;
    private String filter2;

    private Map<String, String> extraInfo;



    public LogFactory() {
        this.messageFactory = createDefaultMessageFactory();
    }


    public static LogFactory start() {
        return new LogFactory();
    }

    public LogFactory withMarker(String category, String subCategory) {
        this.marker = new Marker(env, category, subCategory);
        return this;
    }

    @Deprecated
    public LogFactory withMarker(String module, String category, String subCategory) {
        this.marker = new Marker(module, category, subCategory);
        return this;
    }

    /**
     * "k1={},k2={},k3={}...", v1, v2, v3 ...
     *
     * @param message
     * @param params
     */
    public LogFactory withMessage(final String message, final Object... params) {
        this.message = messageFactory.newMessage(message, params).getFormattedMessage();
        return this;
    }

    public LogFactory withMessage(String message) {
        this.message = message;
        return this;
    }

    public LogFactory withFilter(Object filter1, Object filter2) {
        this.filter1 = filter1 == null ? "" : filter1.toString();
        this.filter2 = filter2 == null ? "" : filter2.toString();
        return this;
    }

    public LogFactory withFilter1(Object filter1) {
        this.filter1 = filter1 == null ? "" : filter1.toString();
        return this;
    }

    public LogFactory withFilter2(Object filter2) {
        this.filter2 = filter2 == null ? "" : filter2.toString();
        return this;
    }

    public LogFactory withException(Exception ex) {
        this.exception = ex;
        return this;
    }

    public LogFactory withExtraInfo(Map<String, String> extraInfo) {
        if (extraInfo == null) {
            return this;
        }
        if (this.extraInfo == null) {
            this.extraInfo = extraInfo;
        } else {
            this.extraInfo.putAll(extraInfo);
        }
        return this;
    }

    public LogFactory withExtraInfo(String key, Object value) {
        if (extraInfo == null) {
            extraInfo = new HashMap<>();
        }
        if (value == null) {
            extraInfo.put(key, "");
        } else {
            extraInfo.put(key, value.toString());
        }
        return this;
    }

    public void debug() {
        Logger.debug(marker, message, extraInfo, filter1, filter2);
    }

    public void info() {
        Logger.info(marker, message, extraInfo, filter1, filter2);
    }

    public void warn() {
        Logger.warn(marker, message, extraInfo, filter1, filter2);
    }

    public void error() {
        Logger.error(marker, message, exception, extraInfo, filter1, filter2);
    }

    private MessageFactory createDefaultMessageFactory() {
        try {
            return DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
        } catch (final InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        LogFactory.env = env;
    }
}
