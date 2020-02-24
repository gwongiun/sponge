package com.ly.add.sponge.ssm.mybatis.plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author  <br/>
 * @date: 2014-12-01 17:28:56 <br/>
 * 
 *  SQL格式化工具,替换“换行”、“制表符”为空格
 */
public class SqlFormat {

    private static Pattern SQL_PATTERN = Pattern.compile("\\s{2,}|\t|\r|\n");

    /**
     * 格式化SQL，替换“换行”、“制表符”为空格
     * 
     * @param sql
     * @return
     */
    public static String format(String sql) {
        String destSql = "";
        if (null != sql && !sql.isEmpty()) {
            Matcher m = SQL_PATTERN.matcher(sql);
            destSql = m.replaceAll(" ");
        }
        return destSql;
    }
}
