package com.ai.baas.omc.topoligy.core.manager.service.db;
import java.util.List;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.OmcCreditService;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcCreditDao;
import com.ai.baas.omc.topoligy.core.persistence.dao.impl.OmcCreditDaoImpl;
import com.ai.baas.omc.topoligy.core.pojo.OmcCredit;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;


public class OmcCreditServiceImpl implements OmcCreditService {
	private static final JdbcProxy dbproxy = JdbcProxy.getInstance();

	/**
	 * 获取信用度
	 * @param tenantid
	 * @param ownetype
	 * @param ownerid
	 * @param resourcecode
	 * @return
     * @throws OmcException
     */
	@Override
	public List<OmcCredit> getAllCredit(String tenantid, String ownetype, String ownerid,String resourcecode) throws OmcException {
		OmcCreditDao omcCreditDao = new OmcCreditDaoImpl();
		return omcCreditDao.selectCredit(dbproxy.getConnection(),tenantid,ownetype,ownerid,resourcecode);
	}
}
