package com.ai.baas.omc.topoligy.core.manager.service.db;

import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcScoutStatusDao;
import com.ai.baas.omc.topoligy.core.persistence.dao.impl.OmcScoutStatusDaoImpl;
import com.ai.baas.omc.topoligy.core.manager.service.ScoutStatusService;
import com.ai.baas.omc.topoligy.core.pojo.OmcScoutStatus;
import com.ai.baas.omc.topoligy.core.pojo.ScoutStatus;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ScoutStatusServiceImpl implements ScoutStatusService {

	public int modifyScoutStatus(Connection connection,ScoutStatus scoutStatus) throws OmcException {
		int nInsertCnt = 0;
		int nRet = 1;
		OmcScoutStatus omcScoutStatus = getRealValue(scoutStatus);
		OmcScoutStatusDao omcScoutStatusDao = new OmcScoutStatusDaoImpl();
		int nUpdateCnt = omcScoutStatusDao.update(connection, omcScoutStatus);
		
		if (nUpdateCnt == 0){
			nInsertCnt = omcScoutStatusDao.insert(connection, omcScoutStatus);
		}
		
		if ((nUpdateCnt == 0)&&(nInsertCnt == 0)){
			throw new OmcException("UPDATE","omcScoutStatus更新失败【" + omcScoutStatus.toString() + "】");
		}
        return nRet;
	}

	@Override
	public ScoutStatus selectStatus(String tenantId,String businessCode, String subsId) throws OmcException {
		Connection connection = JdbcProxy.getInstance().getConnection();
		Map<String, String> params = new HashMap<String, String>();
		params.put(OmcCalKey.OMC_TENANT_ID, tenantId);
		params.put(OmcCalKey.OMC_SUBS_ID, subsId);
		params.put(OmcCalKey.OMC_BUSINESS_CODE, businessCode);
		OmcScoutStatusDao omcScoutStatusDao = new OmcScoutStatusDaoImpl();				
		return getValue(omcScoutStatusDao.selectByparam(connection, params));
	}
	
	
	private ScoutStatus  getValue(OmcScoutStatus omcScoutStatus){
			if (omcScoutStatus == null){
				return null;
			}
			ScoutStatus scoutStatus = new ScoutStatus();
			scoutStatus.setScoSeq(omcScoutStatus.getScoSeq());
			scoutStatus.setAcctId(omcScoutStatus.getAcctId());
			scoutStatus.setCustId(omcScoutStatus.getCustId());
			scoutStatus.setLastStatus(omcScoutStatus.getLastStatus());
			scoutStatus.setBusinessCode(omcScoutStatus.getBusinessCode());
			scoutStatus.setTenantId(omcScoutStatus.getTenantId());
			scoutStatus.setSystemId(omcScoutStatus.getSystemId());
			scoutStatus.setNotifyStatus(omcScoutStatus.getNotifyStatus());
			scoutStatus.setNotifyTime(omcScoutStatus.getNotifyTime());
			scoutStatus.setNotifyTimes(omcScoutStatus.getNotifyTimes());
			scoutStatus.setNotifyType(omcScoutStatus.getNotifyType());
			scoutStatus.setScoutInfo(omcScoutStatus.getScoutInfo());
			scoutStatus.setStatus(omcScoutStatus.getStatus());
			scoutStatus.setStatusTime(omcScoutStatus.getStatusTime());
			scoutStatus.setSubsId(omcScoutStatus.getSubsId());
			
			return scoutStatus;
			
	}
	private OmcScoutStatus getRealValue(ScoutStatus scoutStatus){
		OmcScoutStatus realScoutStatus = new OmcScoutStatus();
		realScoutStatus.setScoSeq(scoutStatus.getScoSeq());
		realScoutStatus.setAcctId(scoutStatus.getAcctId());
		realScoutStatus.setCustId(scoutStatus.getCustId());
		realScoutStatus.setLastStatus(scoutStatus.getLastStatus());
		realScoutStatus.setTenantId(scoutStatus.getTenantId());
		realScoutStatus.setSystemId(scoutStatus.getSystemId());
		realScoutStatus.setBusinessCode(scoutStatus.getBusinessCode());
		realScoutStatus.setNotifyStatus(scoutStatus.getNotifyStatus());
		realScoutStatus.setNotifyTime(scoutStatus.getNotifyTime());
		realScoutStatus.setNotifyTimes(scoutStatus.getNotifyTimes());
		realScoutStatus.setNotifyType(scoutStatus.getNotifyType());
		realScoutStatus.setScoutInfo(scoutStatus.getScoutInfo());
		realScoutStatus.setStatus(scoutStatus.getStatus());
		realScoutStatus.setStatusTime(scoutStatus.getStatusTime());
		realScoutStatus.setSubsId(scoutStatus.getSubsId());
		
		return realScoutStatus;
		
	}
}
