package com.ai.baas.omc.topoligy.core.util.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.zaxxer.hikari.metrics.IMetricsTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class JdbcProxy {

	private static final Logger logger = LoggerFactory.getLogger(JdbcProxy.class);
 	private HikariDataSource datasource;
	private static JdbcProxy instance;
	
	private JdbcProxy(){
	}

	public static JdbcProxy getInstance() {
		if (instance == null){
			logger.error("JdbcProxy还没有初始化");
		}
		return instance;
	}

	public synchronized static void loadresource(JdbcParam jdbcParam) throws OmcException {
		try{
		    HikariConfig config = new HikariConfig();
	        config.setDriverClassName(jdbcParam.getJdbcDriver());
	        config.setJdbcUrl(jdbcParam.getDbUrl());
	        config.addDataSourceProperty("cachePrepStmts", true);
	        config.addDataSourceProperty("prepStmtCacheSize", 500);
	        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
	        config.setConnectionTestQuery("SELECT 1");
	        config.setAutoCommit(true);
	        //池中最小空闲链接数量
	        config.setMinimumIdle(jdbcParam.getInitialConnections());
	        //池中最大链接数量
	        config.setMaximumPoolSize(jdbcParam.getMaxConnections());

	        HikariDataSource ds = new HikariDataSource(config);
	        //设置数据源
			if (instance == null){
				instance = new JdbcProxy();
			}
			instance.setdatabase(ds);
		}catch(Exception e){
			throw new OmcException("初始化连接池异常",e);
		}
	}

	public Connection getConnection() throws OmcException {
		try{
			return datasource.getConnection();
		} catch(Exception e){
			IMetricsTracker tracker = datasource.getMetricsTracker();
			if (tracker != null)
				logger.error("total=" + tracker.getTotalConnections()
						+ ",active=" + tracker.getActiveConnections()
						+ ",idel=" + tracker.getIdleConnections()
						+ ",threadsAwaiting=" + tracker.getThreadsAwaitingConnection()
				);
			logger.error("",e);
			throw new OmcException("获得连接异常",e);
		}
	}

	
	private void setdatabase(HikariDataSource datasource) {
		this.datasource = datasource;
	}

}
