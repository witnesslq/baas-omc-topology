package com.ai.baas.omc.topoligy.core.persistence.dao;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcUrgeStatus;

import java.sql.Connection;
import java.util.Map;


public interface OmcUrgeStatusDao {
	  int insert(Connection connection, OmcUrgeStatus record) throws OmcException;
	  OmcUrgeStatus selectByparam(Connection connection, Map<String, String> param) throws OmcException;
	  int update(Connection connection, OmcUrgeStatus record) throws OmcException;
}
