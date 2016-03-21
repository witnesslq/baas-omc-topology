package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.ScoutStatus;

import java.sql.Connection;

/**
 * 信控状态
 */
public interface ScoutStatusService {
	int modifyScoutStatus(Connection connection, ScoutStatus scoutStatus)  throws OmcException;
	ScoutStatus selectStatus(String tenantId, String businessCode, String subsId) throws OmcException;
}
