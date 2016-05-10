package com.ai.baas.omc.topoligy.core.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ai.baas.dshm.client.CacheFactoryUtil;
import com.ai.baas.dshm.client.impl.CacheBLMapper;
import com.ai.baas.dshm.client.impl.DshmClient;
import com.ai.baas.dshm.client.interfaces.IDshmClient;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;


public final class CacheClient {
	public static final  String delimiter = "#";
//	public static final String CCS_APP_NAME = "ccs.appname";
//	public static final String CCS_ZK_ADDRESS = "ccs.zk_address";
	public static final String PAAS_AUTH_URL = "paas.auth.url";
	public static final String PAAS_AUTH_PID = "paas.auth.pid";
	public static final String PAAS_CCS_SERVICEID = "paas.ccs.serviceid";
	public static final String PAAS_CCS_SERVICE_PASSWORD = "paas.ccs.servicepassword";

	private static ICacheClient cacheSdkClient;
	private static IDshmClient client;
	private static CacheClient cacheClient;
	
	private CacheClient(){}
	
	public static CacheClient getInstance(){
		if (cacheClient==null){
			cacheClient = new CacheClient();
		}
		return cacheClient;
	}
	
	public static void loadResource(Map<String,String> config) throws OmcException {
		getInstance();
		if (cacheSdkClient==null){
			Properties p=new Properties();
			p.setProperty(PAAS_AUTH_URL, config.get(PAAS_AUTH_URL));
			p.setProperty(PAAS_AUTH_PID, config.get(PAAS_AUTH_PID));
			p.setProperty(PAAS_CCS_SERVICEID, config.get(PAAS_CCS_SERVICEID));
			p.setProperty(PAAS_CCS_SERVICE_PASSWORD, config.get(PAAS_CCS_SERVICE_PASSWORD));

			cacheSdkClient =  CacheFactoryUtil
					.getCacheClient(p, CacheBLMapper.CACHE_BL_CAL_PARAM);
		}
	}
	
	public List<Map<String, String>> doQuery(String tableName, Map<String,String> params) throws OmcException{
		if(cacheClient == null){
			throw new OmcException("","cache client connection is null!");
		}
		if(client==null)
			client=new DshmClient();
		List<Map<String, String>> result = null;
		try {
			result = client.list(tableName).where(params).executeQuery(cacheSdkClient);
		} catch (Exception e) {
			throw new OmcException("CacheClient", e);
		}
		return result;
	}
}
