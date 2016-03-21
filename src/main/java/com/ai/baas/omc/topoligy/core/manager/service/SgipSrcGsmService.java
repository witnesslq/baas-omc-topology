package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.SmsInf;

import java.sql.Connection;


public interface SgipSrcGsmService {
	
	int insertMsg(Connection connection, SmsInf smsInf) throws OmcException;
}
