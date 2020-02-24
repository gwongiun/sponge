package com.ly.add.sponge.ssm.mybatis.plugin.apiinfo;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * 对Medusa项目输出接口信息的接口 需实现这个接口的两个方法
 * 请使用以下根路径
 * @RestController
 * @RequestMapping(value = "/apiinfo")
 * 
 * @author hxf14879
 *
 */
public interface ApiInfoBaseApi {

	/**
	 * 输出接口信息，path请务必使用 /queryApiinfo	
	 * 请使用以下注解
	 * @RequestMapping(value = "/queryApiinfo", method = RequestMethod.GET)
     * @InterfaceLog(category = "接口信息", subCategory = "queryApiinfo")
     *  
	 * @param request
	 * @return
	 */
	JSONObject queryApiInfo(HttpServletRequest request) ;
	
	/**
	 * 输出方法信息，path请务必使用 /queryMethodinfo
	 * 请使用以下注解
	 * @RequestMapping(value = "/queryMethodinfo", method = RequestMethod.GET)
     * @InterfaceLog(category = "方法信息", subCategory = "queryMethodinfo")
	 * 
	 * @param request
	 * @return
	 */
	JSONObject queryMethodinfo(HttpServletRequest request);
}
