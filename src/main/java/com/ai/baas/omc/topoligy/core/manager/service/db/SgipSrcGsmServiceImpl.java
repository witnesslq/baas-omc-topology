package com.ai.baas.omc.topoligy.core.manager.service.db;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.SgipSrcGsmService;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcSmsInterfaceDao;
import com.ai.baas.omc.topoligy.core.persistence.dao.impl.OmcSmsInterfaceDaoImpl;
import com.ai.baas.omc.topoligy.core.pojo.SmsInf;

import java.sql.Connection;


public class SgipSrcGsmServiceImpl implements SgipSrcGsmService {
	@Override
	public int insertMsg(Connection connection,SmsInf smsInf) throws OmcException {
		OmcSmsInterfaceDao omcSmsInterfaceDao = new OmcSmsInterfaceDaoImpl();
		return omcSmsInterfaceDao.insert(connection, smsInf);
	}
}
