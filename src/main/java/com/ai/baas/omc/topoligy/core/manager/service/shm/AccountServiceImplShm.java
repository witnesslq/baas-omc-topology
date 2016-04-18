package com.ai.baas.omc.topoligy.core.manager.service.shm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.AccountService;
import com.ai.baas.omc.topoligy.core.pojo.Account;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import org.apache.commons.lang.StringUtils;

/**
 * 获取账户资料信息
 */
public final class AccountServiceImplShm implements AccountService {

	private  static final CacheClient cacheClient = CacheClient.getInstance();
	static final String TABLE_NAME = "bl_acct_info";
	
	@Override
	public Account selectById(String tenantid, String acctId) throws OmcException {
		try{
			Map<String, String> params = new TreeMap<String, String>();
			params.put("ACCT_ID",acctId);
			params.put("TENANT_ID",tenantid);

			List<Map<String, String>> result = cacheClient.doQuery(TABLE_NAME, params);
			if(result == null || result.isEmpty()){
				throw new OmcException("OMC-SUBS0001B","BL_ACCTINFO表没有找到账户信息!");
			}
			return getAccounts(result).get(0);
		}catch (Exception e){
			throw new OmcException("OMC-SUBS0001B",e);
		}
	}
	@Override
	public List<Account> selectBycustId(String tenantid, String custId) throws OmcException {
		try{
			Map<String, String> params = new HashMap<String, String>();
			params.put("CUST_ID",custId);
			params.put("TENANT_ID",tenantid);

			List<Map<String, String>> result = cacheClient.doQuery(TABLE_NAME, params);
			if(result == null || result.size()==0){
				throw new OmcException("OMC-SUBS0001B","BL_ACCTINFO表没有找到账户信息!");
			}
			return getAccounts(result);
		}catch (Exception e){
			throw new OmcException("OMC-SUBS0001B",e);
		}
	}

	private List<Account> getAccounts(List<Map<String, String>> result){
		String[] acctid =	StringUtils.split(result.get(0).get("acct_id"),"#");
		String[] accttype =	StringUtils.split(result.get(0).get("acct_type"),"#");
		String[] ownerTypes = StringUtils.split(result.get(0).get("owner_type"),"#");
		String[] ownerIds =	StringUtils.split(result.get(0).get("owner_id"),"#");
		String[] tenantid = StringUtils.split(result.get(0).get("tenant_id"),"#");

		List<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < acctid.length; i++) {
			Account account = new Account();
			account.setAccountId(acctid[i]);
			account.setOwnerType(ownerTypes[i]);
			account.setOwnerId(ownerIds[i]);
			account.setAcctType(accttype[i]);
			account.setTenantId(tenantid[i]);
			accounts.add(account);
		}
		return accounts;
	}

}
