package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcUrgeStatus;

import java.sql.Connection;


public interface UrgeStatusService {
	int modifyUrgeStatus(Connection connection, OmcUrgeStatus omcUrgeStatus)  throws OmcException;
	OmcUrgeStatus selectUrgeStatus(String tenantId, String businessCode, String urgeType, String ownerType, String ownerId)  throws OmcException;
}
