package com.ai.baas.omc.topoligy.core.util.db;

import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;

import java.util.Map;

/**
 * 数据库连接信息参数
 * @author jackieliu
 *
 */
public final class JdbcParam {
	private  String jdbcDriver = ""; // 数据库驱动
	private  String dbUrl = ""; // 数据 URL
	private static final int initialConnections = 10; // 连接池的初始大小
	private static final int maxConnections = 30; // 连接池最大的大小
	
	public JdbcParam() {
		super();
	}

	public JdbcParam(Map<String, String> paramap){
		this.jdbcDriver = paramap.get(OmcCalKey.OMC_CFG_ENVIRONMENT_DB_DRIVER).toString();
		this.dbUrl = paramap.get(OmcCalKey.OMC_CFG_ENVIRONMENT_DB_URL).toString();
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	public int getInitialConnections() {
		return initialConnections;
	}

	public int getMaxConnections() {
		return maxConnections;
	}
	
	
}
