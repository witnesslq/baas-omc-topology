package com.ai.baas.omc.topoligy.core.util;

import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.runner.center.dshm.api.dshmservice.interfaces.IdshmreadSV;
import com.ai.baas.omc.topoligy.core.constant.ShmConstants;


public final class CacheClient {
	
	public static final String CACHE_IP = "omc.flow.cache.ip";
	public static final String CACHE_PORT = "omc.flow.cache.port";
	public static final  String delimiter = "#";
	
	private static String cacheip;
	private static String cacheport;
	private static IdshmreadSV service;
	private static CacheClient cacheClient;
	
	private CacheClient(){
	}
	
	public static CacheClient getInstance(){
		if (cacheClient==null){
			cacheClient = new CacheClient();
		}
		return cacheClient;
	}
	
	public static void loadResource(Map<String,String> config) throws OmcException {
		if (cacheClient==null){
			cacheClient = new CacheClient();
		}
		try {
			cacheip = config.get(CacheClient.CACHE_IP);
			cacheport = config.get(CacheClient.CACHE_PORT);
			service = (IdshmreadSV) ServiceRegiter.registerService(cacheip, cacheport,
					ShmConstants.ShmServiceCode.SHM_SERVICE_CODE);
		} catch (Exception e) {
			cacheClient = null;
			throw new OmcException("loadResource", e);
		}
	}
	
	public List<Map<String, String>> doQuery(String tableName, Map<String,String> params) throws OmcException{
		if(cacheClient == null){
			throw new OmcException("","cache client connection is null!");
		}
		List<Map<String, String>> result = null;
	
		try {

			result = service.list(tableName).where(params).executeQuery();

		} catch (Exception e) {
			throw new OmcException("CacheClient", e);
		}
		return result;
	}
	
}
