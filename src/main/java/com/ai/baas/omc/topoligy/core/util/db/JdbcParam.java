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
	//默认初始化连接数
	private static final int DEFAULT_INIT_CONN = 10;
	// 连接池的初始大小
	private int initialConnections = DEFAULT_INIT_CONN;
	//默认连接池最大连接数
	private static final int DEFAULT_MAX_CONN = 30;
	// 连接池最大的大小
	private int maxConnections = DEFAULT_MAX_CONN;
	
	public JdbcParam() {
		super();
	}

	public JdbcParam(Map<String, String> paramap){
		this.jdbcDriver = paramap.get(OmcCalKey.OMC_CFG_ENVIRONMENT_DB_DRIVER).toString();
		this.dbUrl = paramap.get(OmcCalKey.OMC_CFG_ENVIRONMENT_DB_URL).toString();
		if (paramap.containsKey(OmcCalKey.OMC_CFG_ENVIRONMENT_DB_INIT_CONN)){
			this.initialConnections = Integer.parseInt(paramap.get(OmcCalKey.OMC_CFG_ENVIRONMENT_DB_INIT_CONN));
		}
		if (paramap.containsKey(OmcCalKey.OMC_CFG_ENVIRONMENT_DB_MAX_CONN)){
			this.maxConnections = Integer.parseInt(paramap.get(OmcCalKey.OMC_CFG_ENVIRONMENT_DB_MAX_CONN));
		}
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
