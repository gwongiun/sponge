package com.ly.add.sponge.common.utils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Created by zh32320 on 2016/12/7.
 */
public class EhCacheUtil {

    private static CacheManager cacheManager;

    public static Object getData(String cacheName, String key) {
        Element element = getCacheManager().getCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }

    public static void putData(String cacheName, String key, Object value, int timeToLiveSeconds) {
        Element element = new Element(key, value);
        element.setTimeToLive(timeToLiveSeconds);
        getCacheManager().getCache(cacheName).put(element);
    }

    private static CacheManager getCacheManager() {
        if (cacheManager == null) {
            cacheManager = CacheManager.create(EhCacheUtil.class.getResource("/ehcache.xml"));
        }
        return cacheManager;
    }

}
