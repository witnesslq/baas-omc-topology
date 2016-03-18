package com.ai.baas.omc.topoligy.core.util.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.DBException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;


public final class JdbcTemplate {
	private JdbcTemplate(){
		
	}

	/**
	 * 查询数据
	 * @param sql
	 * @param conn
	 * @param rsh
	 * @return
	 * @throws SQLException
     * @throws DBException
     */
	public static List<Map<String, Object>> query(String sql, Connection conn, MapListHandler rsh) throws SQLException, DBException {
		boolean autoCommit = conn.getAutoCommit();
		if (!autoCommit){
			conn.setAutoCommit(true);
		}
				
		QueryRunner runner = new QueryRunner();
		List<Map<String, Object>> list = runner.query(conn, sql, rsh);

		if (!autoCommit){
			conn.setAutoCommit(false);
		}
		conn.close();

		return list;
	}

	/**
	 * 更新（包括UPDATE、INSERT、DELETE，返回受影响的行数）
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public static int update(Connection conn,String sql, Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner();
		return runner.update(conn, sql, params);
	}

	public static <T> List<Map<String, Object>> query(Connection conn,String sql,MapListHandler rsh, Object... params) throws SQLException , DBException{
		boolean autoCommit = conn.getAutoCommit();
		if (!autoCommit){
			conn.setAutoCommit(true);
		}
			
		QueryRunner runner = new QueryRunner();
		List<Map<String, Object>> list = runner.query(conn, sql, rsh, params);
		
		if (!autoCommit){
			conn.setAutoCommit(false);
		}
		conn.close();

		return list;

	}

}
