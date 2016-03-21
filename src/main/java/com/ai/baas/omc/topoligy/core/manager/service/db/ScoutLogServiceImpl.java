package com.ai.baas.omc.topoligy.core.manager.service.db;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.ScoutLogService;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcScoutLogDao;
import com.ai.baas.omc.topoligy.core.persistence.dao.impl.OmcScoutLogDaoImpl;
import com.ai.baas.omc.topoligy.core.pojo.OmcScoutLog;
import com.ai.baas.omc.topoligy.core.pojo.ScoutLog;
import com.ai.baas.omc.topoligy.core.util.DateUtils;

import java.sql.Connection;

public class ScoutLogServiceImpl implements ScoutLogService {
	@Override
	public int insertScoutLog(Connection connection,ScoutLog scoutLog) throws OmcException {
		OmcScoutLog omcScoutlog = new OmcScoutLog();
		omcScoutlog.setLogid(scoutLog.getLogid());
		omcScoutlog.setBalanceinfo(scoutLog.getRealTimeBalance().toString());
		omcScoutlog.setBusinessCode(scoutLog.getOwner().getBusinesscode());
		omcScoutlog.setInsettime(DateUtils.currTimeStamp());
		omcScoutlog.setOwnerId(scoutLog.getOwner().getOwerid());
		omcScoutlog.setOwnertype(scoutLog.getOwner().getOwertype());
		omcScoutlog.setSystemId("system_id");
		omcScoutlog.setTenantId(scoutLog.getOwner().getTenantid());
		omcScoutlog.setScoutRule(scoutLog.getSectionRules().toString());
		omcScoutlog.setScoutType(scoutLog.getSourceType());
		omcScoutlog.setSourcetype(scoutLog.getSourceType());
		omcScoutlog.setStatus(scoutLog.getScostatus());
		
		OmcScoutLogDao omcScoutLogDao = new OmcScoutLogDaoImpl();
		
		return omcScoutLogDao.insert(connection, omcScoutlog);
	}
}
