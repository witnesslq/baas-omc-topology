package com.ai.baas.omc.topoligy.core.persistence.dao;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.SmsInf;

import java.sql.Connection;


public interface OmcSmsInterfaceDao {
	int insert(Connection connection, SmsInf record) throws OmcException;
}
