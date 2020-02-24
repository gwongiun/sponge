package com.ly.add.sponge.ssm.mybatis.plugin.apiinfo;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 自动收集接口和方法注解中的信息
 * @author hxf14879
 *
 */
@Component
public class ApiInfoHandler implements ApplicationListener<ContextRefreshedEvent> {

	private static JSONArray methodInfoList = new JSONArray();

	private static JSONArray apiInfoList = new JSONArray();
	
	private static JSONArray reqParamsList = new JSONArray();

	private static JSONArray respParamsList = new JSONArray();
	
	/**
	 * handle apiBeans and methodBeans
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {//root容器为spring容器
			Map<String, Object> methodOperationBeans = event.getApplicationContext().getBeansWithAnnotation(MethodInfo.class);
			//methodbeans:{api1070Service=com.ly.mdd.api.service.Api1070Service@70c77674}
			for (Entry<String, Object> methodBean : methodOperationBeans.entrySet()) {
				try {
					String beanName = methodBean.getValue().toString();
					if (beanName.indexOf("@") != -1) {
						Class clazz = Class.forName(beanName.split("@")[0]);
						collectMethodInfo(clazz);//收集方法信息
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (event.getApplicationContext().getParent() != null) {//dispatcher容器
			Map<String, Object> apiOperationBeans = event.getApplicationContext().getBeansWithAnnotation(Api.class);
			//System.out.println("apibeans :"+apiOperationBeans);
			//apibeans :{api1070Controller=com.ly.mdd.api.controller.Api1070Controller@1d3142a0}
			for (Entry<String, Object> apiBean : apiOperationBeans.entrySet()) {
				try {
					String beanName = apiBean.getValue().toString();
					if (beanName.indexOf("@") != -1) {
						Class clazz = Class.forName(beanName.split("@")[0]);
						collectApiInfo(clazz);//收集接口信息
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String getPathFromRequestMapping(RequestMapping rm) {
		String path = "";
		if (rm != null) {
			String[] paths = rm.value();
			if (paths != null && paths.length>0) {
				path = paths[0];
			}
		}
		return path;
	}

	/**
	 * 收集接口信息
	 * @param value
	 */
	private void collectApiInfo(Class clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		RequestMapping rm = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
		String path = getPathFromRequestMapping(rm);
		for (Method method : methods) {
			ApiOperation ainfo = method.getAnnotation(ApiOperation.class);//接口信息
			RequestMapping rm2 = method.getAnnotation(RequestMapping.class);//接口路径
			path += getPathFromRequestMapping(rm2);
			if (ainfo != null) {//匹配到ApiOperation注解 收集接口信息
				JSONObject apiInfo = new JSONObject();
				apiInfo.put("name", ainfo.value());//接口的名称标识
				apiInfo.put("description", ainfo.notes());//接口描述
				apiInfo.put("title", ainfo.notes());//接口标题
				apiInfo.put("serviceName", path);//接口路径
				apiInfoList.add(apiInfo);
				//System.out.println("add apiInfo:"+apiInfo);
			}
		}
		//System.out.println("apiInfoList:"+apiInfoList);
	}

	/**
	 * 收集方法信息
	 * @param bean
	 */
	private void collectMethodInfo(Class clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			MethodInfo minfo = method.getAnnotation(MethodInfo.class);
			if (minfo != null) {//匹配到methodinfo注解 收集方法信息
				JSONObject methodInfo = new JSONObject();
				methodInfo.put("api", minfo.api());//方法对应的api名称
				methodInfo.put("name", method.getName());//直接使用方法名
				methodInfo.put("parentName", minfo.parentMethod());//方法的父方法名称标识
				methodInfo.put("fullName", clazz.getName()+"_"+method.getName());
				methodInfo.put("description", minfo.description());//方法的描述
				methodInfoList.add(methodInfo);
				//System.out.println("add methodInfo:"+methodInfo);
			}
		}
		//System.out.println("methodInfoList:"+methodInfoList);
	}
	
	/**
	 * 返回收集好的方法信息列表
	 * @return
	 */
	public static JSONArray getMethodInfoList() {
		return methodInfoList;
	}
	
	/**
	 * 返回收集好的接口信息列表
	 * @return
	 */
	public static JSONArray getApiInfoList() {
		return apiInfoList;
	}
	
	/**
	 * 返回收集到的接口入参列表
	 * @return
	 */
	public static JSONArray getReqParamsList() {
		return reqParamsList;
	}

	/**
	 * 返回收集到的接口出参列表
	 * @return
	 */
	public static JSONArray getRespParamsList() {
		return respParamsList;
	}
	
}
