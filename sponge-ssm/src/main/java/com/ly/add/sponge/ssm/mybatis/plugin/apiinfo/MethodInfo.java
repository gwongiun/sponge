package com.ly.add.sponge.ssm.mybatis.plugin.apiinfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法信息注解定义，用于收集方法信息
 * <p>
 * 方法是指service、dao这些数据访问层的方法
 * 例如调用链为controller1-service1-dao1，且controller1的swagger.value=Api1070
 * service1注解:
 * @MethodInfo(api="Api1070", name="service1", description="XX业务逻辑处理")
 * dao1注解:
 * @MethodInfo(api="Api1070", name="dao1", parentMethod="service1" , description="XX业务逻辑处理")
 * <p>
 * @author hxf14879
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodInfo {

	/**
	 * method所属api标识，必须与接口controller中的swagger.value属性一致，如Api1070
	 */
	String api() default "";
	
	/**
	 * method父节点，如果是第一层不用传，默认置空字符串，否则传父节点MethodInfo.name
	 */
	String parentMethod() default "";
	
//	/**
//	 * method标识，默认使用方法名称作为标识
//	 */
//	String name() default "";
	
	/**
	 * method中文描述
	 */
	String description() default "";
}
