package com.ly.add.sponge.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 打印异常信息的帮助类
 * @author hxf14879
 *
 */
public class ExceptionUtil {
	
	/**
	 * 把异常的堆栈信息打印到string后返回该string
	 * @param e
	 * @return
	 */
	public static String getErrorInfoFromException(Exception e) {  
        StringWriter sw = new StringWriter();
		try (PrintWriter pw = new PrintWriter(sw)) {
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e1) {
			e1.printStackTrace();
			return "bad getErrorInfoFromException";
		}
	}
}
