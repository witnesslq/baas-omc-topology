package com.ai.baas.omc.topoligy.core.manager.service;


import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.SpeUrgeStop;

public interface SpeUrgeStopService {
	/**
	 * 获得免催停信息
	 * @param tenantid
	 * @param ownertype
	 * @param ownerid
	 * @return
	 * @throws OmcException
     */
	SpeUrgeStop selectById(String tenantid, String ownertype, String ownerid) throws OmcException;
}