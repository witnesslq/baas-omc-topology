package com.ai.baas.omc.topoligy.core.persistence.dao;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcBmsInterface;

import java.sql.Connection;


public interface OmcBmsInterfaceDao {
	int insert(Connection connection, OmcBmsInterface record) throws OmcException;
}
