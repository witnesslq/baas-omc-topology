package com.ai.baas.omc.topoligy.core.persistence.dao.impl;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcSmsInterfaceDao;
import com.ai.baas.omc.topoligy.core.pojo.SmsInf;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 信控短信接口
 */
public class OmcSmsInterfaceDaoImpl implements OmcSmsInterfaceDao {

	@Override
	public int insert(Connection connection, SmsInf record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_urge_interface";
		sql.append("INSERT INTO ");
		sql.append(tablename);
		sql.append(" (serial_no,tenant_id,system_id,owner_type,owner_id,urge_info,insert_time,retry_times,deal_flag,deal_time,remark)");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,?,?,?)");
		
		Object[] params = convert(record);

		try{

			return JdbcTemplate.update(connection,sql.toString(),params);
		
		}catch(SQLException e){
			throw new OmcException("信控预警接口保存异常",sql.toString() + Arrays.toString(params),e);
		}
	}
	private Object[] convert(SmsInf data){

		Object[] params = new Object[11];
		params[0] = data.getSerialno();
		params[1] = data.getTenantid();		
		params[2] = data.getSystemid();
		params[3] = data.getOwnertype();
		params[4] = data.getOwnerid();
		params[5] = data.getUrgeinfo();
		params[6] = data.getInserttime();
		params[7] = data.getRetrytime();		
		params[8] = data.getDealflag();
		params[9] = data.getDealtime();
		params[10] = data.getRemark();
		
		return params;
	}
}
