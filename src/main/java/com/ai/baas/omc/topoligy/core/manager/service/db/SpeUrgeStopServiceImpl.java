package com.ai.baas.omc.topoligy.core.manager.service.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.SpeUrgeStopService;
import com.ai.baas.omc.topoligy.core.pojo.SpeUrgeStop;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SpeUrgeStopServiceImpl implements SpeUrgeStopService {
	
	private  static final Logger logger = LoggerFactory.getLogger(SpeUrgeStopServiceImpl.class);
	private static final JdbcProxy dbproxy = JdbcProxy.getInstance();

	@Override
	public SpeUrgeStop selectById(String tenantid, String ownertype, String ownerid) throws OmcException {
		SpeUrgeStop speUrgeStop = null;
		try {
			StringBuilder sql = new StringBuilder();
			String tableName ="omc_avoidscout";
			String ownerType1 = ownertype;
			
			if (StringUtils.startsWith(ownertype, "/")){
				ownerType1 = StringUtils.substring(ownertype, 1);
			}
			
			sql.append("select avoid_seq, spe_type, eff_date,exp_date from ");
			sql.append(tableName).append(" where ");
			sql.append(" owner_type = ? and owner_id = ? and tenant_id = ?");
			sql.append(" and  spe_type !=").append("'").append("REDLIST").append("'");
			sql.append(" and  now() between eff_date and exp_date");

			Object[] para = new Object[3];
			para[0] = ownerType1;
			para[1] = ownerid;
			para[2] = tenantid;
			
			Connection connection = dbproxy.getConnection();
   		    List<Map<String,Object>> rows =  JdbcTemplate.query(connection, sql.toString(), new MapListHandler(), para);
   		    if ((rows == null)||(rows.isEmpty())){
				logger.info("免催停没获得数据【omc_avoidscout】");
				return speUrgeStop;
   		    }
			Map<String, Object> map = rows.get(0);
			speUrgeStop = new SpeUrgeStop();
			speUrgeStop.setOwnerid(ownerid);
			speUrgeStop.setOwnertype(ownertype);
			speUrgeStop.setTenantid(tenantid);
			speUrgeStop.setSpeType(map.get("spe_type").toString());
			speUrgeStop.setEffDate(DateUtils.getTimestamp(map.get("eff_date").toString(), "yyyy-MM-dd HH:mm:ss"));
			speUrgeStop.setExpDate(DateUtils.getTimestamp(map.get("exp_date").toString(), "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			throw new OmcException("SpeUrgeStopServiceImpl",e);
		}
		return speUrgeStop;
	}
}
