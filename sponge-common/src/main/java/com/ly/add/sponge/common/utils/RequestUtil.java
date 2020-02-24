package com.ly.add.sponge.common.utils;

import com.ly.add.sponge.common.tuple.TwoTuple;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : qqy48861
 * date : 2019/5/23.
 */
public class RequestUtil {

    public static TwoTuple<String, String> extractIPnUA(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null) {
            ip = ip.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            try {
                ip = request.getRemoteAddr();
            } catch (Exception e) {
                return new TwoTuple<>("", "");
            }
        }
        String ua = request.getHeader("user-agent");
        return new TwoTuple<>(ip, ua);
    }

}