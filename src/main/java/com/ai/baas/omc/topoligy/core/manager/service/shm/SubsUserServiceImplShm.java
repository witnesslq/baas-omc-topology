package com.ai.baas.omc.topoligy.core.manager.service.shm;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.SubsUserService;
import com.ai.baas.omc.topoligy.core.pojo.User;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
* @ClassName: SubsUserServiceImpl 
* @Description: 获取资料信息 
* @author lvsj
* @date 2015年11月24日 下午5:16:23 
*
 */
public final class SubsUserServiceImplShm implements SubsUserService {
	
	private  static final CacheClient cacheClient = CacheClient.getInstance();
	@Override
	public User selectById(String tenantid, String id) throws OmcException {
		try{
			StringBuilder table = new StringBuilder();
			
			table.append("bl_userinfo");
			
			Map<String, String> params = new TreeMap<String, String>();
			
			params.put("SUBS_ID",id);
			params.put("TENANT_ID",tenantid);

			List<Map<String, String>> result = cacheClient.doQuery(table.toString(), params);
			
			if(result == null || result.size()==0){
				throw new OmcException("OMC-SUBS0001B","subs_user表没有找到用户信息!" + params.toString());
			}
			
			return  getUsers(result).get(0);
			

		}catch (Exception e){
			throw new OmcException("OMC-RULE0001B",e);
		}
	}

	@Override
	public List<User> selectByAcctId(String tenantid, String id)  throws OmcException{
		
		try{
			StringBuilder table = new StringBuilder();
			
			table.append("bl_userinfo");
			
			Map<String, String> params = new TreeMap<String, String>();
			
			params.put("ACCT_ID",id);
			params.put("TENANT_ID",tenantid);

			List<Map<String, String>> result = cacheClient.doQuery(table.toString(), params);
			
			if(result == null || result.size()==0){
				throw new OmcException("OMC-SUBS0001B","bl_userinfo表没有找到用户信息!" + params.toString());
			}
			
			return  getUsers(result);
			

		}catch (Exception e){
			throw new OmcException("OMC-RULE0001B",e);
		}
	}

	@Override
	public List<User> selectByCustId(String tenantid,  String id)  throws OmcException{
		try{
			StringBuilder table = new StringBuilder();
			
			table.append("bl_userinfo");
			
			Map<String, String> params = new TreeMap<String, String>();
			
			params.put("CUST_ID",id);
			params.put("TENANT_ID",tenantid);

			
			List<Map<String, String>> result = cacheClient.doQuery(table.toString(), params);
			
			if(result == null || result.size()==0){
				throw new OmcException("OMC-SUBS0001B","subs_user表没有找到用户信息!"+ params.toString());
			}
			
			return  getUsers(result);
		}catch (Exception e){
			throw new OmcException("OMC-RULE0001B",e);
		}
	}

	@Override
	public User selectByNbr(String tenantid,  String nbr)  throws OmcException{
		try{
			StringBuilder table = new StringBuilder();
			
			table.append("bl_userinfo");
			
			Map<String, String> params = new TreeMap<String, String>();
			
			params.put("SERVICE_NUM",nbr);
			params.put("TENANT_ID",tenantid);
			

			List<Map<String, String>> result = cacheClient.doQuery(table.toString(), params);
			
			if(result == null || result.size()==0){
				throw new OmcException("OMC-SUBS0001B","subs_user表没有找到用户信息!"+ params.toString());
			}
			
			return  getUsers(result).get(0);
			

		}catch (Exception e){
			throw new OmcException("OMC-RULE0001B",e);
		}
	}
	
	

	private List<User> getUsers(List<Map<String, String>> result){
		
		String[] subsid =	StringUtils.split(result.get(0).get("subs_id"),"#");
		String[] custid =	StringUtils.split(result.get(0).get("cust_id"),"#");
		String[] acctid =	StringUtils.split(result.get(0).get("acct_id"),"#");
		String[] tenantid =	StringUtils.split(result.get(0).get("tenant_id"),"#");
		String[] systemId =	StringUtils.split(result.get(0).get("system_id"),"#");		
		String[] servicenum =	StringUtils.split(result.get(0).get("service_num"),"#");
		String[] basicorgid =	StringUtils.split(result.get(0).get("basic_org_id"),"#");
		String[] provincecode =	StringUtils.split(result.get(0).get("province_code"),"#");
		String[] citycode =	StringUtils.split(result.get(0).get("city_code"),"#");
		String[] activetime =	StringUtils.split(result.get(0).get("active_time"),"#");
		String[] inactivetime =	StringUtils.split(result.get(0).get("inactive_time"),"#");
		String[] factorcode =	StringUtils.split(result.get(0).get("factor_code"),"#");
		
		  
		
		List<User> users = new ArrayList<User>();
		
		for (int i = 0; i < subsid.length; i++) {
			User user = new User();
			user.setAccountid(acctid[i]);
			user.setBasicorgid(basicorgid[i]);
			user.setCitycode(citycode[i]);
			user.setCustomerid(custid[i]);
			user.setProvincecode(provincecode[i]);
			user.setServicenum(servicenum[i]);
			user.setSubsid(subsid[i]);
			user.setSystemid(systemId[i]);
			user.setTenantid(tenantid[i]);
			user.setFactorcode(factorcode[i]);

			String s2 = StringUtils.isBlank(activetime[i])?"1900-01-01 00:00:00.000":activetime[i];
			user.setActivetime(Timestamp.valueOf(s2));
			String s3 = StringUtils.isBlank(inactivetime[i])?"1900-01-01 00:00:00.000":inactivetime[i];
			user.setInactivetime(Timestamp.valueOf(s3));
		
			users.add(user);
		}
		
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User subsUser = iterator.next();
			if (DateUtils.currTimeStamp().after(subsUser.getActivetime())
				&& DateUtils.currTimeStamp().before(subsUser.getInactivetime())){
				continue;
			}else{
				iterator.remove();
			}
		}
		
		if (users.isEmpty()){
			return Collections.emptyList();
		}
		return users;
	}


}