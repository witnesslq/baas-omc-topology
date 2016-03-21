package com.ai.baas.omc.topoligy.core.persistence.dao.impl;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcBmsInterfaceDao;
import com.ai.baas.omc.topoligy.core.pojo.OmcBmsInterface;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 停开机接口表
 */
public class OmcBmsInterfaceDaoImpl implements OmcBmsInterfaceDao {
	private Logger logger = LoggerFactory.getLogger(OmcBmsInterfaceDaoImpl.class);

	@Override
	public int insert(Connection connection, OmcBmsInterface record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_bms_interface";
		sql.append("INSERT INTO ");
		sql.append(tablename);
		sql.append(" (serial_no,acct_id,subs_id,bms_data,interface_data,scout_type,");
		sql.append(" insert_time,deal_flag,deal_time,remark,retry_times,tenant_id,system_id");
		sql.append(" )VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		Object[] params = convert(record);
		try{
			return JdbcTemplate.update(connection,sql.toString(),params);
		}catch(SQLException e){
			logger.error("install table omc_bms_interface error;",e);
			throw new OmcException("插入停开机接口表异常",sql.toString() + Arrays.toString(params),e);
		}
	}
	
	private Object[] convert(OmcBmsInterface record){
		Object[] params = new Object[13];
		params[0] = record.getSerialNo();
		params[1] = record.getAcctId();
		params[2] = record.getSubsId();
		params[3] = record.getBmsData();		
		params[4] = record.getInterfaceData();
		params[5] = record.getScoutType();		
		params[6] = record.getInsertTime();
		params[7] = record.getDealFlag();
		params[8] = record.getDealTime();
		params[9] = record.getRemark();
		params[10] = record.getRetryTimes();
		params[11] = record.getTenantId();
		params[12] = record.getSystemId();
		return params;
	}

}
