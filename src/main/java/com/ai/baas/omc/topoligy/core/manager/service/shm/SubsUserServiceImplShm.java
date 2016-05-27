package com.ai.baas.omc.topoligy.core.manager.service.shm;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.SubsUserService;
import com.ai.baas.omc.topoligy.core.pojo.User;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import com.ai.baas.omc.topoligy.core.util.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;

/**
 * 
* @ClassName: SubsUserServiceImpl 
* @Description: 获取用户资料信息
* @author lvsj
* @date 2015年11月24日 下午5:16:23 
*
 */
public final class SubsUserServiceImplShm implements SubsUserService {
	private static Logger LOGGER = LoggerFactory.getLogger(SubsUserServiceImplShm.class);
	private static final CacheClient cacheClient = CacheClient.getInstance();
	//用户信息表表名
	private static final String USER_TABLE = "bl_userinfo";
	//用户信息扩展表表名
	private static final String USER_INFO_EXT = "bl_userinfo_ext";
	//提醒号码在扩展表中名称
	private static final String REMIND_NUM = "remind_num";
	@Override
	public User selectById(String tenantid, String id) throws OmcException {
		try{
			Map<String, String> params = new TreeMap<String, String>();
			params.put("SUBS_ID",id);
			params.put("TENANT_ID",tenantid);

			List<Map<String, String>> result = cacheClient.doQuery(USER_TABLE, params);
			if(result == null || result.size()==0){
				throw new OmcException("OMC-SUBS0001B","subs_user表没有找到用户信息!" + params.toString());
			}
			return  getUsers(result).get(0);
		}catch (Exception e){
			throw new OmcException("OMC-RULE0001B",e);
		}
	}

	/**
	 * 根据账户id查询用户列表
	 * @param tenantid
	 * @param id
	 * @return
	 * @throws OmcException
     */
	@Override
	public List<User> selectByAcctId(String tenantid, String id)  throws OmcException{
		
		try{
			Map<String, String> params = new TreeMap<String, String>();
			params.put("ACCT_ID",id);
			params.put("TENANT_ID",tenantid);

			List<Map<String, String>> result = cacheClient.doQuery(USER_TABLE, params);
			if(result == null || result.size()==0){
				throw new OmcException("OMC-SUBS0001B","bl_userinfo表没有找到用户信息!" + params.toString());
			}
			return  getUsers(result);
		}catch (Exception e){
			throw new OmcException("OMC-RULE0001B",e);
		}
	}

	/**
	 * 通过客户id查询用户列表
	 * @param tenantid
	 * @param id
	 * @return
	 * @throws OmcException
     */
	@Override
	public List<User> selectByCustId(String tenantid,  String id)  throws OmcException{
		try{
			Map<String, String> params = new TreeMap<String, String>();
			params.put("CUST_ID",id);
			params.put("TENANT_ID",tenantid);

			List<Map<String, String>> result = cacheClient.doQuery(USER_TABLE, params);
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
			Map<String, String> params = new TreeMap<String, String>();
			params.put("SERVICE_ID",nbr);
			params.put("TENANT_ID",tenantid);

			List<Map<String, String>> result = cacheClient.doQuery(USER_TABLE, params);
			if(result == null || result.isEmpty()){
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
		//service_id由service_num变化来
		String[] serviceIds =	StringUtils.split(result.get(0).get("service_id"),"#");
		String[] provincecode =	StringUtils.split(result.get(0).get("province_code"),"#");
		String[] citycode =	StringUtils.split(result.get(0).get("city_code"),"#");
		String[] activetime =	StringUtils.split(result.get(0).get("active_time"),"#");
		String[] inactivetime =	StringUtils.split(result.get(0).get("inactive_time"),"#");
		String[] policy_id =	StringUtils.split(result.get(0).get("policy_id"),"#");

		List<User> users = new ArrayList<User>();
		for (int i = 0; i < subsid.length; i++) {
			User user = new User();
			user.setAccountid(acctid[i]);
			user.setCitycode(citycode[i]);
			user.setCustomerid(custid[i]);
			user.setProvincecode(provincecode[i]);
			user.setServiceId(serviceIds[i]);
			user.setSubsid(subsid[i]);
			/*
			 * 此属性已取消,为兼容原数据设计,将此项设置为常量
			 * updateDate 2016-03-22
			 */
			user.setSystemid("1");
			user.setTenantid(tenantid[i]);
			/**
			 * 此属性修改为从扩展表中获取
			 * updateDate 2016-03-24
			 */
			user.setFactorcode(getRemindNum(subsid[i]));
			String s2 = StringUtils.isBlank(activetime[i])?"1900-01-01 00:00:00.000":activetime[i];
			user.setActivetime(Timestamp.valueOf(s2));
			String s3 = StringUtils.isBlank(inactivetime[i])?"1900-01-01 00:00:00.000":inactivetime[i];
			user.setInactivetime(Timestamp.valueOf(s3));
			if (policy_id!=null) {
				if (policy_id.length>i) {
					user.setPolicy_id(policy_id[i]);
				}
			}
			users.add(user);
		}
		
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User subsUser = iterator.next();
			Timestamp nowTime = DateUtils.currTimeStamp();
			//如果当前时间在用户生效时间之前或失效时间之后,则用户无效
			if (nowTime.before(subsUser.getActivetime())
				&& nowTime.after(subsUser.getInactivetime())){
				iterator.remove();
			}
		}
		
		if (users.isEmpty()){
			users = Collections.emptyList();
		}
		return users;
	}

	/**
	 * 从用户信息扩展表中获取提醒号码
	 * @param subsId
	 * @return
	 * @addDate 2016-03-24
     */
	public String getRemindNum(String subsId) {
		Map<String, String> params = new TreeMap<String, String>();
		params.put("EXT_NAME", REMIND_NUM);
		params.put("SUBS_ID", subsId);
		String remindNum = null;
		try {
			List<Map<String, String>> result = cacheClient.doQuery(USER_INFO_EXT, params);
			if (result!=null && result.size()>0){
				String[] extVals = StringUtils.split(result.get(0).get("ext_value"),"#");
				remindNum = extVals[0];
			}
		} catch (OmcException e) {
			LOGGER.error("",e);
		}
		return remindNum;
	}

}
