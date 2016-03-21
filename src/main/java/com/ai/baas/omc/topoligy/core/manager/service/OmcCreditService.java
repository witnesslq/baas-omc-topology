package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcCredit;

import java.util.List;

public interface OmcCreditService{
	/**
	 * 获取信用度
	 * @param tenantid
	 * @param ownetype
	 * @param ownerid
	 * @param resourcecode
	 * @return
	 * @throws OmcException
	 */
	List<OmcCredit>  getAllCredit(String tenantid, String ownetype, String ownerid, String resourcecode) throws OmcException;
}
