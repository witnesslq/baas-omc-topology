package com.ai.baas.omc.topoligy.core.persistence.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcScoutStatusDao;
import com.ai.baas.omc.topoligy.core.pojo.OmcScoutStatus;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * 信控状态操作
 */
public final class OmcScoutStatusDaoImpl implements OmcScoutStatusDao {

	@Override
	public int insert(Connection connection, OmcScoutStatus record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_scout_status";
		sql.append("INSERT INTO ");
		sql.append(tablename);
		sql.append(" (sco_seq, tenant_id, system_id, subs_id, acct_id, cust_id, business_code, status, last_status,");
		sql.append(" status_time, notify_time, notify_times, notify_status, notify_type, scout_info) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		Object[] params = convert(record);

		try{
			int nret = JdbcTemplate.update(connection,sql.toString(),params);
			if (nret >0){
				return movetobackup(connection, record);				
			}else{
				return nret;
			}

		}catch(SQLException e){
			throw new OmcException("更新失败",sql.toString() + Arrays.toString(params),e);
		}
	}

	/**
	 * 根据用户id,租户id,业务类型查询信控状态
	 * @param connection
	 * @param param
	 * @return
	 * @throws OmcException
     */
	@Override
	public OmcScoutStatus selectByparam(Connection connection, Map<String, String> param) throws OmcException {
		
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_scout_status";
		sql.append("select  ");
		sql.append(" sco_seq, tenant_id, system_id, subs_id, acct_id, cust_id, business_code, status, last_status,");
		sql.append(" status_time, notify_time, notify_times, notify_status, notify_type, scout_info ");
		sql.append(" from ");
		sql.append(tablename);
		sql.append(" where subs_id = ? and tenant_id = ? and business_code = ?");
		Object[] params = new Object[3];

		params[0] = param.get("subs_id");
		params[1] = param.get("tenant_id");
		params[2] = param.get("business_code");
		try {
			List<Map<String, Object>> retMap = JdbcTemplate.query(connection, sql.toString(), new MapListHandler(), params);
			if (retMap == null || retMap.isEmpty()) {
				return null;
			}

			OmcScoutStatus omcScoutStatus = new OmcScoutStatus();
			Map<String, Object> map = retMap.get(0);
			omcScoutStatus.setAcctId(map.get("acct_id").toString());
			omcScoutStatus.setBusinessCode(map.get("business_code").toString());
			omcScoutStatus.setCustId(map.get("cust_id").toString());
			omcScoutStatus.setLastStatus(map.get("last_status").toString());
			omcScoutStatus.setNotifyStatus(map.get("notify_status").toString());
			omcScoutStatus.setNotifyType(map.get("notify_type").toString());
			omcScoutStatus.setScoutInfo(map.get("scout_info").toString());
			omcScoutStatus.setStatus(map.get("status").toString());
			omcScoutStatus.setSubsId(map.get("subs_id").toString());
			omcScoutStatus.setSystemId(map.get("system_id").toString());
			omcScoutStatus.setTenantId(map.get("tenant_id").toString());
			String notifytimes = (map.get("notify_times") == null) ? "0" : map.get("notify_times").toString();
			String ScoSeq = (map.get("sco_seq") == null) ? "0" : map.get("sco_seq").toString();

			omcScoutStatus.setNotifyTimes(Integer.parseInt(notifytimes));
			omcScoutStatus.setScoSeq(Long.parseLong(ScoSeq));
			omcScoutStatus.setStatusTime(DateUtils.getTimestamp(map.get("status_time").toString(), "yyyy-MM-dd HH:mm:ss"));
			omcScoutStatus.setNotifyTime(DateUtils.getTimestamp(map.get("notify_time").toString(), "yyyy-MM-dd HH:mm:ss"));


			return omcScoutStatus;

		} catch (Exception e) {
			throw new OmcException("获取信控状态异常", sql.toString() + Arrays.toString(params), e);
		}
	}

	@Override
	public int update(Connection connection, OmcScoutStatus record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_scout_status";
		sql.append("update ");
		sql.append(tablename);
		sql.append(" set status = ?, last_status = ?,");
		sql.append(" status_time = ?, notify_time = ?, notify_times = ?, notify_status = ?,scout_info = ? ");
		sql.append(" where  sco_seq = ?");

		Object[] params = new Object[8];
		params[0] = record.getStatus();
		params[1] = record.getLastStatus();
		params[2] = record.getStatusTime();
		params[3] = record.getNotifyTime();
		params[4] = record.getNotifyTimes();
		params[5] = record.getNotifyStatus();
		params[6] = record.getScoutInfo();
		params[7] = record.getScoSeq();
		
		try{
			int nret = JdbcTemplate.update(connection,sql.toString(),params);
			if (nret>0){
				return movetobackup(connection, record);
			}else{
				return nret;
			}
		}catch(SQLException e){
			throw new OmcException("更新信控状态异常",sql.toString() + Arrays.toString(params),e);
		}
	}

	private int movetobackup(Connection connection,OmcScoutStatus record) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "omc_scout_status" ;
		String currmonth = DateUtils.getCurrMonth();
		sql.append("insert into  ");
		sql.append(tablename).append("_").append(currmonth);
		sql.append(" (sco_seq ,tenant_id,system_id,subs_id,acct_id,cust_id,business_code,status");
		sql.append(",last_status,status_time,notify_time,notify_times,notify_status,notify_type");
		sql.append(",scout_info,bak_time)");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())");
		Object[] params = convert(record);
		try{
			return JdbcTemplate.update(connection,sql.toString(),params);
		}catch(SQLException e){
			throw new OmcException("备份信控状态异常",sql.toString() + Arrays.toString(params),e);
		}
	}
	private Object[] convert(OmcScoutStatus record){
		Object[] params = new Object[15];
		params[0] = record.getScoSeq();
		params[1] = record.getTenantId();
		params[2] = record.getSystemId();
		params[3] = record.getSubsId();		
		params[4] = record.getAcctId();
		params[5] = record.getCustId();		
		params[6] = record.getBusinessCode();
		params[7] = record.getStatus();
		params[8] = record.getLastStatus();
		params[9] = record.getStatusTime();
		params[10] = record.getNotifyTime();		
		params[11] = record.getNotifyTimes();
		params[12] = record.getNotifyStatus();
		params[13] = record.getNotifyType();
		params[14] = record.getScoutInfo();
		return params;
	}

}
