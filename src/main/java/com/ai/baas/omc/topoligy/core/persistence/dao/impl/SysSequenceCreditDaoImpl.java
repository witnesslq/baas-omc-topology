package com.ai.baas.omc.topoligy.core.persistence.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.persistence.dao.SysSequenceCreditDao;
import com.ai.baas.omc.topoligy.core.pojo.SysSequenceCredit;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * 系统序列号
 */
public class SysSequenceCreditDaoImpl implements SysSequenceCreditDao {
	
	@Override
	public SysSequenceCredit selectByKey(Connection connection, String key) throws OmcException {
		StringBuilder sql = new StringBuilder();
		String tablename = "sys_sequence_credit";
		sql.append("select  ");
		sql.append(" sequence_name,current_value,min_value,max_value,is_cycle");
		sql.append(" from ");
		sql.append(tablename);
		sql.append(" where sequence_name = ? ");
		Object[] params = new Object[1];

		params[0] = key;
		try{
			
			List<Map<String, Object>> retMap = JdbcTemplate.query(connection,sql.toString(), new MapListHandler(),params);
			if ((retMap == null)||(retMap.isEmpty())){
				return null;
			}
			
			SysSequenceCredit sequenceCredit  = new SysSequenceCredit();
			for (Iterator<Map<String, Object>> iterator = retMap.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
	    		
				String currentvalue = (map.get("current_value") == null) ? "0":map.get("current_value").toString()  ;
	    		String minvalue = (map.get("min_value") == null) ? "0":map.get("min_value").toString()  ;
	    		String maxvalue = (map.get("max_value") == null) ? "0":map.get("max_value").toString()  ;
	    		
				sequenceCredit.setCurrentValue(Long.parseLong(currentvalue));
				sequenceCredit.setIsCycle(map.get("is_cycle").toString());
				sequenceCredit.setMaxValue(Long.parseLong(maxvalue));
				sequenceCredit.setMinValue(Long.parseLong(minvalue));
				sequenceCredit.setSequenceName(map.get("sequence_name").toString());
				
				break;
			}
		
			return sequenceCredit;

		}catch(Exception e){
			throw new OmcException("获取序列异常",sql.toString() + Arrays.toString(params),e);
		}
	}

	@Override
	public int update(Connection connection, SysSequenceCredit record) throws OmcException {
		StringBuilder sql = new StringBuilder();

		String tablename = "sys_sequence_credit";
		sql.append(" update ");
		sql.append(tablename);
		sql.append(" set current_value = ?");
		sql.append(" where sequence_name = ? ");

		Object[] params = new Object[2];
		params[0] = record.getCurrentValue();
		params[1] = record.getSequenceName();

		try{

			return JdbcTemplate.update(connection,sql.toString(),params);
		
		}catch(SQLException e){
			
			throw new OmcException("更新序列表异常",sql.toString() + Arrays.toString(params),e);
		}
	}
}
