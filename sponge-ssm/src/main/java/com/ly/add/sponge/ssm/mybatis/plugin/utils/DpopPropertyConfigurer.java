package com.ly.add.sponge.ssm.mybatis.plugin.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 * ClassName: DpopPropertyConfigurer <br/>
 * date: 2014-7-22 上午10:50:41 <br/>
 *
 * @author 
 * @version 
 * @since JDK 1.6
 */
public class DpopPropertyConfigurer extends PropertyPlaceholderConfigurer {

    private static Map<String,String> properties = new ConcurrentHashMap<String,String>();
    
    private static DpopPropertyPlaceholderHelper helper = new DpopPropertyPlaceholderHelper(DEFAULT_PLACEHOLDER_PREFIX,
            DEFAULT_PLACEHOLDER_SUFFIX,
            DEFAULT_VALUE_SEPARATOR, false);
    
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
         throws BeansException {

        for (Entry<Object, Object> entry : props.entrySet()) {
            String stringKey = String.valueOf(entry.getKey());
            String stringValue = String.valueOf(entry.getValue());
            stringValue = helper.replacePlaceholders(stringValue, props);
            properties.put(stringKey, stringValue);
        }
        super.processProperties(beanFactoryToProcess, props);
    }

    /**
     * 获取所有的配置项映射表
     * @return
     */
    public static Map<String, String> getProperties() {  
        return Collections.unmodifiableMap(properties);  
    }  
    
    /**
     * 获取配置项的值
     * @param key
     * @return
     */
    public static String getProperty(String key){  
        return properties.get(key);  
    }
    
    
    /**
     * 替换，类似:
     * DpopPropertyConfigurer.replacePlaceholders("${name}");
     * @param key
     * @return
     */
    public static String replacePlaceholders(String key) {
    	return helper.replacePlaceholders(key, properties);
    }
    
    
    /**
     * 对Spring的PropertyPlaceholderHelper类扩展支持map操作
     * @author 
     *
     */
    static class DpopPropertyPlaceholderHelper extends PropertyPlaceholderHelper {

		public DpopPropertyPlaceholderHelper(String placeholderPrefix,
				String placeholderSuffix, String valueSeparator,
				boolean ignoreUnresolvablePlaceholders) {
			super(placeholderPrefix, placeholderSuffix, valueSeparator,
					ignoreUnresolvablePlaceholders);
		}
    	
		public String replacePlaceholders(String value, final Map<String,String> mapProperties) {
			Assert.notNull(properties, "Argument 'properties' must not be null.");
			return replacePlaceholders(value, new PlaceholderResolver() {
				public String resolvePlaceholder(String placeholderName) {
					return mapProperties.get(placeholderName);
				}
			});
		}
		
    }
    
    public static void main(String[] args){
    	Properties props = new Properties();
    	props.put("name", "huangxiaofeng");
    	props.put("uit", "1");
    	props.put("na1me", "huangxiaofeng1221");
    	
        System.out.println(helper.replacePlaceholders("${name}", props));
        System.out.println(helper.replacePlaceholders("${uit}", props));
    	System.out.println(helper.replacePlaceholders("${na${uit}me}", props));
    	
    	System.out.println("--------");
    	
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("name", "huangxiaofeng");
    	map.put("uit", "1");
    	map.put("na1me", "huangxiaofeng1221");
        System.out.println(helper.replacePlaceholders("${name}", map));
        System.out.println(helper.replacePlaceholders("${uit}", map));
    	System.out.println(helper.replacePlaceholders("${na${uit}me}", map));
    	
    }
}
