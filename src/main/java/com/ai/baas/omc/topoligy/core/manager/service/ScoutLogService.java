package com.ai.baas.omc.topoligy.core.manager.service;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.ScoutLog;

import java.sql.Connection;


public interface ScoutLogService {

	int insertScoutLog(Connection connection, ScoutLog scoutLog) throws OmcException;
}
