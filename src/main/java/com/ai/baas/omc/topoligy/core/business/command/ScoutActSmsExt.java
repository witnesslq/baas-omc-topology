package com.ai.baas.omc.topoligy.core.business.command;

import com.ai.baas.omc.topoligy.core.business.InformationProcessor;
import com.ai.baas.omc.topoligy.core.constant.OwnerType;
import com.ai.baas.omc.topoligy.core.constant.ScoStatus;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.*;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.google.gson.JsonObject;
/**
 * 
* @ClassName: ScoutActSmsExt 
* @Description: 根据预警状态判断是否需要生成预警信息
* @author lvsj
* @date 2015年11月17日 下午3:08:34 
*
 */

public class ScoutActSmsExt extends ScoutActSms {

	public ScoutActSmsExt(OmcObj owner, InformationProcessor info, ConfigContainer cfg, RealTimeBalance balance, JsonObject data){
		super(owner,info,cfg,balance,data);
	}
	
	/**
	 * 
	* @Title: warning 
	*  
	* @param @param user
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int warning(String ownertype,String oid)  throws OmcException {

		//根据本地信控状态判断是否需要预警，对于提醒到用户的需要判断用户资料的状态，提醒的账户和客户的不判断资料
		if ((ownertype.equals(OwnerType.SERV))){
			ScoutStatus scoutStatus = scoutStatusService.selectStatus(this.getOmcobj().getTenantid(),this.getOmcobj().getBusinesscode(),oid);
			if (scoutStatus != null){
				if ((scoutStatus.getStatus().equals(ScoStatus.STOP))||
						(scoutStatus.getStatus().equals(ScoStatus.HALFSTOP ))||
						(scoutStatus.getStatus().equals(ScoStatus.DELAYSTOP))){
					logger.info("此对象已经停机，不再进行预警提醒"+"ownertype:["+ownertype+"] oid:["+ oid);
					return 0;
				}
			}
		}
		//获取当前预警状态
		OmcUrgeStatus omcUrgeStatus = urgeStatusService.selectUrgeStatus(this.getOmcobj().getTenantid(),this.getOmcobj().getBusinesscode(), this.getSectionRule().getSectiontype() , ownertype, oid);
		if ((omcUrgeStatus != null)&&(ScoStatus.WARNING.equals(omcUrgeStatus.getStatus()))){
			if (omcUrgeStatus.getNotifyTimes() > 0){
				logger.info("此对象已经提醒过，不再重复提醒"+"ownertype:["+ownertype+"] oid:["+ oid);
				return 0;
			}
		}
		//获取当前信控状态
		SectionRule sectionRule = this.getSectionRule();
		
		if (omcUrgeStatus == null){
			omcUrgeStatus = new OmcUrgeStatus();
			omcUrgeStatus.setUrgeSerial(0L);
			omcUrgeStatus.setTenantId(this.getOmcobj().getTenantid());
			omcUrgeStatus.setSystemId("SystemId");
				
			omcUrgeStatus.setBusinessCode(this.getOmcobj().getBusinesscode());
			omcUrgeStatus.setUrgeType(sectionRule.getSectiontype());
			omcUrgeStatus.setOwnerId(oid);
			omcUrgeStatus.setOwnerType(ownertype);
			
			omcUrgeStatus.setNotifyType(sectionRule.getSectiontype());
			omcUrgeStatus.setScoutInfo("后续处理");
			omcUrgeStatus.setStatus(ScoStatus.WARNING);
			omcUrgeStatus.setStatusTime(DateUtils.currTimeStamp());
			omcUrgeStatus.setLastStatus(ScoStatus.WARNOFF);
			omcUrgeStatus.setNotifyStatus("1");
			omcUrgeStatus.setNotifyTime(DateUtils.currTimeStamp());
			omcUrgeStatus.setNotifyTimes(1);
		}else{
			omcUrgeStatus.setNotifyType(sectionRule.getSectiontype());
			omcUrgeStatus.setScoutInfo("后续处理");
			omcUrgeStatus.setStatus(ScoStatus.WARNING);
			omcUrgeStatus.setStatusTime(DateUtils.currTimeStamp());
			omcUrgeStatus.setLastStatus(ScoStatus.WARNOFF);
			omcUrgeStatus.setNotifyStatus("1");
			omcUrgeStatus.setNotifyTime(DateUtils.currTimeStamp());
			omcUrgeStatus.setNotifyTimes(1);

		}
		this.setMyomcUrgeStatus(omcUrgeStatus);

		return super.warning(ownertype,oid);			

	}	
	
	public int warnoff(String ownertype,String oid)  throws OmcException {

		//获取当前预警状态
		OmcUrgeStatus  omcUrgeStatus = urgeStatusService.selectUrgeStatus(this.getOmcobj().getTenantid(),this.getOmcobj().getBusinesscode(), this.getSectionRule().getSectiontype() , ownertype, oid);
		
		if (omcUrgeStatus == null
				|| ScoStatus.WARNOFF.equals(omcUrgeStatus.getStatus())){
			return 0;
		}
		//获取当前信控状态
		SectionRule sectionRule = this.getSectionRule();
		omcUrgeStatus.setNotifyType(sectionRule.getSectiontype());
		omcUrgeStatus.setScoutInfo("后续处理");
		omcUrgeStatus.setStatus(ScoStatus.WARNOFF);
		omcUrgeStatus.setStatusTime(DateUtils.currTimeStamp());
		omcUrgeStatus.setLastStatus(ScoStatus.WARNING);
		omcUrgeStatus.setNotifyStatus("1");
		omcUrgeStatus.setNotifyTime(DateUtils.currTimeStamp());
		omcUrgeStatus.setNotifyTimes(1);

		this.setMyomcUrgeStatus(omcUrgeStatus);

		return super.warnoff(ownertype,oid);			

	}	

}
