package com.ai.baas.omc.topoligy.core.manager.service.db;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.ScoutBmsInterfaceService;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcBmsInterfaceDao;
import com.ai.baas.omc.topoligy.core.persistence.dao.impl.OmcBmsInterfaceDaoImpl;
import com.ai.baas.omc.topoligy.core.pojo.OmcBmsInterface;

import java.sql.Connection;

/**
 * 
* @ClassName: ScoutBmsInterfaceServiceImpl 
* @Description: 写入停机接口表
* @author lvsj
* @date 2015年10月26日 下午12:27:11 
*
 */
public class ScoutBmsInterfaceServiceImpl implements ScoutBmsInterfaceService {
	@Override
	public int addInterFace(Connection connection,OmcBmsInterface omcBmsInterface) throws OmcException {
		OmcBmsInterfaceDao omcBmsInterfaceDao = new OmcBmsInterfaceDaoImpl();
		return omcBmsInterfaceDao.insert(connection, omcBmsInterface);
	}
}
