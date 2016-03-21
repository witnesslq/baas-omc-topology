package com.ai.baas.omc.topoligy.core.persistence.dao;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcScoutLog;

import java.sql.Connection;


public interface OmcScoutLogDao {
	int insert(Connection connection, OmcScoutLog record) throws OmcException;
}
