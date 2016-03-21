package com.ai.baas.omc.topoligy.core.persistence.dao.impl;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcScoutLogDao;
import com.ai.baas.omc.topoligy.core.pojo.OmcScoutLog;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 信控日志操作
 */
public class OmcScoutLogDaoImpl implements OmcScoutLogDao {

	@Override
	public int insert(Connection connection, OmcScoutLog record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String currmonth = DateUtils.getCurrMonth();
		String tablename = "omc_scout_log";
		sql.append("INSERT INTO ");
		sql.append(tablename).append("_").append(currmonth);
		sql.append(" (logid,SourceType,ownertype,owner_id,business_code,scout_type,status");
		sql.append(" ,insettime,scout_rule,balanceinfo,parainfo,tenant_id,system_id)");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?)");
   
		Object[] params = convert(record);

		try{
			return JdbcTemplate.update(connection,sql.toString(),params);
		}catch(SQLException e){
			throw new OmcException("信控日志记录异常",sql.toString() + Arrays.toString(params),e);
		}
	}
	private Object[] convert(OmcScoutLog record){
		Object[] params = new Object[13];
		params[0] = record.getLogid();
		params[1] = record.getSourcetype();
		params[2] = record.getOwnertype();
		params[3] = record.getOwnerId();
		params[4] = record.getBusinessCode();
		params[5] = record.getScoutType();		
		params[6] = record.getStatus();
		params[7] = record.getInsettime();
		params[8] = record.getScoutRule();
		params[9] = record.getBalanceinfo();
		params[10] = record.getParainfo();
		params[11] = record.getTenantId();
		params[12] = record.getSystemId();

		return params;
	}
}
