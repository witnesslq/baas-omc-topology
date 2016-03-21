package com.ai.baas.omc.topoligy.core.persistence.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcUrgeStatusDao;
import com.ai.baas.omc.topoligy.core.pojo.OmcUrgeStatus;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * 催缴状态操作
 */
public final class OmcUrgeStatusDaoImpl implements OmcUrgeStatusDao {
	
	@Override
	public int insert(Connection connection, OmcUrgeStatus record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_urge_status";
		sql.append("INSERT INTO ");
		sql.append(tablename);
		sql.append(" (urge_serial, owner_type, owner_id, urge_type, business_code, status, last_status, status_time, notify_time,");
		sql.append("notify_times, notify_status, notify_type, scout_info, system_id, tenant_id )");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		Object[] params = convert(record);

		try{
			int nret = JdbcTemplate.update(connection,sql.toString(),params);
			if (nret >0 ){
				return movetobackup(connection, record);
			}else{
				return nret;
			}
				
		}catch(SQLException e){
			throw new OmcException("更新失败",sql.toString() + Arrays.toString(params),e);
		}
	}

	@Override
	public OmcUrgeStatus selectByparam(Connection connection, Map<String, String> param) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_urge_status";
		sql.append("select  ");
		sql.append(" urge_serial, owner_type, owner_id, urge_type, business_code, status, last_status, status_time, notify_time,");
		sql.append("notify_times, notify_status, notify_type, scout_info, system_id, tenant_id ");
		sql.append(" from ");
		sql.append(tablename);
		sql.append(" where tenant_id = ? and owner_type = ? and owner_id = ? and business_code = ? and urge_type = ?");
		Object[] params = new Object[5];
		
		params[0] = param.get("tenant_id");
		params[1] = param.get("owner_type");
		params[2] = param.get("owner_id");
		params[3] = param.get("business_code");
		params[4] = param.get("urge_type");
		try{
			List<Map<String, Object>> retMap = JdbcTemplate.query(connection,sql.toString(), new MapListHandler(),params);			
			if ((retMap == null)||(retMap.isEmpty())){
				return null;
			}
			
			OmcUrgeStatus omcUrgeStatus = new OmcUrgeStatus();
			for (Iterator<Map<String, Object>> iterator = retMap.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				
				omcUrgeStatus.setBusinessCode(map.get("business_code").toString());
				omcUrgeStatus.setLastStatus(map.get("last_status").toString());
				omcUrgeStatus.setNotifyStatus(map.get("notify_status").toString());
				omcUrgeStatus.setNotifyType(map.get("notify_type").toString());
				omcUrgeStatus.setOwnerId(map.get("owner_id").toString());
				omcUrgeStatus.setOwnerType(map.get("owner_type").toString());
				omcUrgeStatus.setScoutInfo(map.get("scout_info").toString());
				omcUrgeStatus.setStatus(map.get("status").toString());
				omcUrgeStatus.setSystemId(map.get("system_id").toString());
				omcUrgeStatus.setTenantId(map.get("tenant_id").toString());
				omcUrgeStatus.setUrgeType(map.get("urge_type").toString());
				omcUrgeStatus.setNotifyTime(DateUtils.getTimestamp(map.get("notify_time").toString(), "yyyy-MM-dd HH:mm:ss"));
				omcUrgeStatus.setStatusTime(DateUtils.getTimestamp(map.get("status_time").toString(), "yyyy-MM-dd HH:mm:ss"));

				String notifytimes = (map.get("notify_times") == null) ? "0":map.get("notify_times").toString()  ;
	    		String ScoSeq = (map.get("urge_serial") == null) ? "0":map.get("urge_serial").toString()  ;
			
				omcUrgeStatus.setUrgeSerial(Long.parseLong(ScoSeq));				
				omcUrgeStatus.setNotifyTimes(Integer.parseInt(notifytimes));
				break;
			}
		
			return omcUrgeStatus;

		}catch(Exception e){
			throw new OmcException("获取预警状态异常",sql.toString() + Arrays.toString(params),e);
		}
	}

	@Override
	public int update(Connection connection, OmcUrgeStatus record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_urge_status";
		sql.append("update ");
		sql.append(tablename);
		sql.append(" set status = ?, last_status = ?,");
		sql.append(" status_time = ?, notify_time = ?, notify_times = ?, notify_status = ?,scout_info = ?,notify_type = ? ");
		sql.append(" where  urge_serial = ?");
		
		Object[] params = new Object[9];

		params[0] = record.getStatus();
		params[1] = record.getLastStatus();
		params[2] = record.getStatusTime();
		params[3] = record.getNotifyTime();
		params[4] = record.getNotifyTimes();
		params[5] = record.getNotifyStatus();
		params[6] = record.getScoutInfo();
		params[7] = record.getNotifyType();
		params[8] = record.getUrgeSerial();
		
		try{
			int nret = JdbcTemplate.update(connection,sql.toString(),params);
			if (nret > 0){
				return movetobackup(connection, record);
			}else{
				return nret;
			}
		}catch(SQLException e){
			throw new OmcException("更新信控状态异常",sql.toString() + Arrays.toString(params),e);
		}
	}
	
	private int movetobackup(Connection connection,OmcUrgeStatus record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_urge_status" ;
		String currmonth = DateUtils.getCurrMonth();
		sql.append("insert into  ");
		sql.append(tablename).append("_").append(currmonth);
		sql.append(" (urge_serial, owner_type, owner_id, urge_type, business_code, status, last_status, status_time, notify_time,");
		sql.append("notify_times, notify_status, notify_type, scout_info, system_id, tenant_id,bak_time ");
		sql.append(" ) VALUES ( ");
		sql.append(" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())");
		
		Object[] params = convert(record);
		try{
			return JdbcTemplate.update(connection,sql.toString(),params);
		}catch(SQLException e){
			throw new OmcException("备份预警状态异常",sql.toString() + Arrays.toString(params),e);
		}
	}
	
	private Object[] convert(OmcUrgeStatus record){
		Object[] params = new Object[15];
		params[0] = record.getUrgeSerial();
		params[1] = record.getOwnerType();
		params[2] = record.getOwnerId();
		params[3] = record.getUrgeType();		
		params[4] = record.getBusinessCode();
		params[5] = record.getStatus();		
		params[6] = record.getLastStatus();
		params[7] = record.getStatusTime();
		params[8] = record.getNotifyTime();
		params[9] = record.getNotifyTimes();
		params[10] = record.getNotifyStatus();
		params[11] = record.getNotifyType();
		params[12] = record.getScoutInfo();
		params[13] = record.getSystemId();
		params[14] = record.getTenantId();
		return params;
	}
}
