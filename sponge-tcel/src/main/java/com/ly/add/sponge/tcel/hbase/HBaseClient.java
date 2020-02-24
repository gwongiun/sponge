package com.ly.add.sponge.tcel.hbase;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ly.add.sponge.common.utils.ExceptionUtil;
import com.ly.add.sponge.tcel.log.LogFactory;
import com.ly.dc.sdk.client.HGet;
import com.ly.dc.sdk.client.HTable;
import org.apache.directory.api.util.Strings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionManager;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;

import java.util.List;

/**
 * Hbase 工厂
 * @author wb6214
 */
public class HBaseClient {

	private static Connection connection;

	public HBaseClient() {
		Configuration conf = HBaseConfiguration.create();
		try {
			connection = ConnectionManager.getConnection(conf);
		} catch (Exception e) {
			LogFactory.start()
					.withMarker("初始化HBase", "异常")
					.withMessage("错误信息={}", ExceptionUtil.getErrorInfoFromException(e))
					.error();
		}
	}

	/**
	 * 获取hbase表的实例化对象 此方法建议用于读取数据时
	 * @return 表的实例，没有则返回null
	 */
	public HTable getHTable(String tableName) throws Exception {
        return connection.getHTable(tableName);
	}

	public Result[] queryFromHBase(String tableName, List<String> rowKeyArr, String boName, boolean isMd5Hash) {
		Result[] results = null;
		List<HGet> gets = Lists.newArrayList();
		long start = System.currentTimeMillis();
		HTable htable = null;
        try {
            htable = connection.getHTable(tableName);
			rowKeyArr.stream().filter(Strings::isNotEmpty).
					forEach(rowKey -> gets.add(new HGet(Bytes.toBytes(isMd5Hash ? MD5Hash.getMD5AsHex(Bytes.toBytes(rowKey)) : rowKey))));
			results = htable.get(gets);
			long end = System.currentTimeMillis();
			/* 天网系统 */
			LogFactory.start()
					.withMarker(boName, "查询HBase")
					.withFilter1(boName)
					.withMessage("HBase耗时={}, filter1={}, filter2={}", end - start, boName, tableName)
					.withExtraInfo("ESrequest rowKey: ", JSON.toJSONString(rowKeyArr))
					.withExtraInfo("ESresponse:", results)
					.info();
		} catch (Exception e) {
			LogFactory.start()
					.withMarker(boName, "查询HBase异常")
					.withFilter(boName, tableName)
					.withMessage("filter1={}, filter2={}", boName, tableName)
					.withExtraInfo("ESrequest rowKey: ", JSON.toJSONString(rowKeyArr))
					.withExtraInfo("Exception:", ExceptionUtil.getErrorInfoFromException(e))
					.error();
		} finally {
			try {
				closeHTable(htable);
			} catch (Exception e) {
				// 记录日志
				LogFactory.start()
						.withMarker(boName, "关闭HBase连接")
						.withMessage("关闭HTable异常:{}", ExceptionUtil.getErrorInfoFromException(e))
						.withException(e)
						.error();
			}
		}
		return results;
	}

	public void closeHTable(HTable hTable) throws Exception{
		if (null != hTable) {
			hTable.close();
		}
	}

}