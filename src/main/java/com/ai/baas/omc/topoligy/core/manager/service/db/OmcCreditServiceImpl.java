package com.ai.baas.omc.topoligy.core.manager.service.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.OmcCreditService;
import com.ai.baas.omc.topoligy.core.pojo.OmcCredit;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OmcCreditServiceImpl implements OmcCreditService {
	
	private  static final Logger logger = LoggerFactory.getLogger(OmcCreditServiceImpl.class);
	
	private static final JdbcProxy dbproxy = JdbcProxy.getInstance();

	/**
	 * 获取信用度
	 * @param tenantid
	 * @param ownetype
	 * @param ownerid
	 * @param resourcecode
	 * @return
     * @throws OmcException
     */
	@Override
	public List<OmcCredit> getAllCredit(String tenantid, String ownetype, String ownerid,String resourcecode) throws OmcException {
		try {
			StringBuilder sql = new StringBuilder("");
			String tableName ="omc_creditfee";
  
			sql.append("select credit_seq, credit_type, credit_line,eff_time,exp_time from ");
			sql.append(tableName).append(" where ");
			sql.append(" tenant_id =").append("'").append(tenantid).append("'");
			sql.append(" and  owner_type =").append("'").append(ownetype).append("'");
			sql.append(" and  owner_id =").append("'").append(ownerid).append("'");
			sql.append(" and  resource_code =").append("'").append(resourcecode).append("'");
			
			Connection connection = dbproxy.getConnection();
			
   		    List<Map<String,Object>> rows =  JdbcTemplate.query(sql.toString(), connection, new MapListHandler());
   		    
   		    if ((rows == null)||(rows.isEmpty())){
				logger.info("信用度没取到数据【omc_creditfee】");
				return Collections.emptyList();
   		    }
   		    
   		    List<OmcCredit> omcCredits = new ArrayList<OmcCredit>();

	    	for (Map<String, Object> map : rows) {
	    		
	    		OmcCredit omcCredit = new OmcCredit();
	    		omcCredit.setOwnerid(ownerid);
	    		omcCredit.setOwnertype(ownetype);
	    		omcCredit.setTenantid(tenantid);
	    		String creditline = (map.get("credit_line") == null) ? "0.0":map.get("credit_line").toString()  ;
	    		omcCredit.setCreditline(Double.parseDouble(creditline));
	    		omcCredit.setCredittype(map.get("credit_type").toString());
	    		String efftime = map.get("eff_time").toString();
	    		String exptime = map.get("exp_time").toString();
	    		omcCredit.setEffDate(DateUtils.getTimestamp(efftime, "yyyy-MM-dd HH:mm:ss"));
	    		omcCredit.setExpDate(DateUtils.getTimestamp(exptime, "yyyy-MM-dd HH:mm:ss"));
	    		
	    		omcCredits.add(omcCredit);

			}
	    	return omcCredits;

		} catch (Exception e) {
			throw new OmcException("ConfigObtain",e);
		}
	}
}
