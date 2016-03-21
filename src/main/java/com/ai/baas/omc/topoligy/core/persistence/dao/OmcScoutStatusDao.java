package com.ai.baas.omc.topoligy.core.persistence.dao;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcScoutStatus;

import java.sql.Connection;
import java.util.Map;


/**
 * 信控状态操作
 */
public interface OmcScoutStatusDao {
	  int insert(Connection connection, OmcScoutStatus record) throws OmcException;
	  OmcScoutStatus selectByparam(Connection connection, Map<String, String> param) throws OmcException;
	  int update(Connection connection, OmcScoutStatus record) throws OmcException;
}
