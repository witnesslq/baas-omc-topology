package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcBmsInterface;

import java.sql.Connection;


public interface ScoutBmsInterfaceService {
	
	int addInterFace(Connection connection, OmcBmsInterface omcBmsInterface) throws OmcException;

}
