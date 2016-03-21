package com.ai.baas.omc.topoligy.core.persistence.dao;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.SysSequenceCredit;

import java.sql.Connection;


public interface SysSequenceCreditDao {
	SysSequenceCredit selectByKey(Connection connection, String key) throws OmcException;
	int update(Connection connection, SysSequenceCredit record) throws OmcException;
}
