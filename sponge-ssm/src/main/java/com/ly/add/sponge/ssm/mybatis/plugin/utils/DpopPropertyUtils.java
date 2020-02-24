package com.ly.add.sponge.ssm.mybatis.plugin.utils;

import org.springframework.util.StringUtils;

/**
 * 
 * ClassName: DpopPropertyUtils <br/>
 * date: 2014-7-22 上午10:58:31 <br/>
 *
 * @author 
 * @version 
 * @since JDK 1.6
 */
public class DpopPropertyUtils {
	
	
	

	/**
	 * 
	 * @param key
	 * @return
	 */
    public static String getProperty(String key) {
        try {
            return DpopPropertyConfigurer.getProperty(key);
        } catch (Exception e) {
            return key;
        }
    }
    
    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        try {
            String value = DpopPropertyConfigurer.getProperty(key);
            
            return (null == value) ? defaultValue : value;
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    /**
     * 替换，类似:
     * DpopPropertyUtils.replacePlaceholders("${name}","huangxiaofeng");
     * @param key
     * @param defaultValue
     * @return
     */
    public static String replacePlaceholders(String key, String defaultValue) {
    	try{
    		if(!StringUtils.hasLength(key)){
    			return defaultValue;
    		}
    		
    		String value = DpopPropertyConfigurer.replacePlaceholders(key);
    		if(null == value){
    			return defaultValue;
    		}
    		return value;
    	}catch(Exception e){
    		return defaultValue;
    	}
    }
    
}
