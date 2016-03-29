package com.ai.baas.omc.topoligy.core.business.command;

import java.sql.Timestamp;

import com.ai.baas.omc.topoligy.core.business.command.rule.ActionRule;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.constant.ScoRuleType;
import com.ai.baas.omc.topoligy.core.constant.ScoStatus;
import com.ai.baas.omc.topoligy.core.constant.rule.YesNo;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.RealTimeBalance;
import com.ai.baas.omc.topoligy.core.pojo.ScoutStatus;
import com.ai.baas.omc.topoligy.core.pojo.User;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;

public class ScoutActBmsExt extends ScoutActBms {

	public ScoutActBmsExt(OmcObj owner, ConfigContainer cfg, RealTimeBalance balance, JsonObject data){
		super(owner,cfg, balance, data);
	}
	
	@Override
	public int stop(User user)  throws OmcException {
		//资料类判断
		if (!ActionRule.canAction(ScoRuleType.STOP, user,this.getConfig())){
			return 0;
		}
		//获取信控状态
		ScoutStatus scoutStatus = scoutStatusService.selectStatus(user.getTenantid(), this.getOmcobj().getBusinesscode(), user.getSubsid()) ;
		//若信控状态为stop,则直接返回
		if ((scoutStatus != null)&&(scoutStatus.getStatus().equals(ScoStatus.STOP))){
			return 1;
		}
		//判断非延迟停机配置的直接进入停机处理
		//获得租户允许延迟停机时间
		Integer delayTimes = getDelaytimes(OmcCalKey.OMC_CFG_DELAYSTOP);
//		if (!checkDelay()){
//			return 0;
//		}
		//不存在延迟停机
		if ( delayTimes <= 0 ){
			//todo 待添加单停处理
			if (scoutStatus == null){
				myscoutStatus = this.newScoStatus(user, ScoStatus.STOP);
			}else{
				myscoutStatus = this.modiScoStatus(scoutStatus, ScoStatus.STOP);
			}
			return super.stop(user);
		}
		
		//存在延迟停机
		if ((scoutStatus == null)
				||(!scoutStatus.getStatus().equals(ScoStatus.DELAYSTOP))){
			if (scoutStatus == null){
				myscoutStatus = this.newScoStatus(user, ScoStatus.DELAYSTOP);
			}else{
				myscoutStatus = this.modiScoStatus(scoutStatus, ScoStatus.DELAYSTOP);
			}
			return 1;
		//已经是延迟停机状态
		}else if(scoutStatus.getStatus().equals(ScoStatus.DELAYSTOP)){
			//todo双停判断
			long lastTime = scoutStatus.getStatusTime().getTime();
			long currTime = DateUtils.currTimeStamp().getTime();
			final long SECONDS_PER_MINUS = 1l * 1000 * 60;
			//若现在时间大于信控状态时间加上延迟时间,则进行将信控状态设置为stop
			if ((currTime - lastTime) <= (delayTimes * SECONDS_PER_MINUS)){
				myscoutStatus = this.modiScoStatus(scoutStatus, ScoStatus.DELAYSTOP);
				return super.stop(user);
			}
		}
		return 1;
	}

	@Override
	public int start(User user)  throws OmcException {
		//判断资料是否可以开机
		if (!ActionRule.canAction(ScoRuleType.START, user,this.getConfig())){
			return 0;
		}
		//根据信控信控状态判断是否可以开机（为避免资料同步延迟，对于资料正常开机的不再判断资料）
		ScoutStatus scoutStatus = scoutStatusService.selectStatus(user.getTenantid(), this.getOmcobj().getBusinesscode(), user.getSubsid());
		//若信控状态不存在或已经为start,则直接返回
		if (scoutStatus == null
				|| ScoStatus.START.equals(scoutStatus.getStatus())){
			return 1;
		}
		myscoutStatus = this.modiScoStatus(scoutStatus, ScoStatus.START);
		return super.start(user);
	}

	@Override
	public int halfstop(User user)  throws OmcException {
		if (!ActionRule.canAction(ScoRuleType.HALFSTOP, user,this.getConfig())){
			return 0;
		}
		
		Integer delayTimes = getDelaytimes(OmcCalKey.OMC_CFG_DELAYHALFSTOP);
		//根据本地信控状态判断是否需要单停信控
		ScoutStatus scoutStatus = scoutStatusService.selectStatus(
				user.getTenantid(),this.getOmcobj().getBusinesscode(), user.getSubsid());

		//对于已经停机 单停 双缓的用户不再进行后续操作
		if ((scoutStatus != null)
				&&((scoutStatus.getStatus().equals(ScoStatus.STOP))||
									(scoutStatus.getStatus().equals(ScoStatus.HALFSTOP ))||
									(scoutStatus.getStatus().equals(ScoStatus.DELAYSTOP)))){
			return 0;
		}

		//非延迟停机
		if ( delayTimes <=0 ){
			//todo 待添加单停处理
			if (scoutStatus == null){
				String currStatus = ScoStatus.HALFSTOP;
				myscoutStatus = this.newScoStatus(user, currStatus);

			}else{
				String currStatus = ScoStatus.HALFSTOP;
				myscoutStatus = this.modiScoStatus(scoutStatus, currStatus);
			}
			return super.halfstop(user);
		}
		
		//存在延迟停机
		if ((scoutStatus == null)||(!scoutStatus.getStatus().equals(ScoStatus.DELAYHALFSTOP))){
			if (scoutStatus == null){
				String currStatus = ScoStatus.DELAYSTOP;
				myscoutStatus = this.newScoStatus(user, currStatus);

			}else{
				String currStatus = ScoStatus.DELAYSTOP;
				myscoutStatus = this.modiScoStatus(scoutStatus, currStatus);
			}
			return 1;
		}else if(scoutStatus.getStatus().equals(ScoStatus.DELAYHALFSTOP)){
			//todo双停判断
			Timestamp lastTime = scoutStatus.getStatusTime();
			Timestamp currTime = DateUtils.currTimeStamp();
			final long SECONDS_PER_MINUS = 1l * 1000 * 60;
			if ((currTime.getTime() - lastTime.getTime()) <= (delayTimes * SECONDS_PER_MINUS)){
				String currStatus = ScoStatus.DELAYHALFSTOP;
				myscoutStatus = this.modiScoStatus(scoutStatus, currStatus);
				return super.halfstop(user);
			}
		}
		
/**
 */
		return 1;
	}
	/**
	 * @throws OmcException 
	 * 
	* @Title: warning 
	* @Description :sejugmujing xinxiS
	*  
	* @param @param user
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int warning(User user) throws OmcException {
		if (!ActionRule.canAction(ScoRuleType.WARNING, user,this.getConfig())){
			return 0;
		}
		
		//根据本地信控状态判断是否需要单停信控

		ScoutStatus scoutStatus = scoutStatusService.selectStatus(user.getTenantid(),this.getOmcobj().getBusinesscode(), user.getSubsid());

		//对于已经停机 单停 双缓的用户不再进行后续操作
		if ((scoutStatus != null)&&((scoutStatus.getStatus().equals(ScoStatus.STOP))||
									(scoutStatus.getStatus().equals(ScoStatus.HALFSTOP ))||
									(scoutStatus.getStatus().equals(ScoStatus.DELAYSTOP)))){
			return 0;
		}
		//查找指定号码
		
		
		//todo:: 待添加催缴的处理
			if (scoutStatus == null){
				String currStatus = ScoStatus.WARNING;
				myscoutStatus = this.newScoStatus(user, currStatus);

			}else{
				String currStatus = ScoStatus.WARNING;
				myscoutStatus = this.modiScoStatus(scoutStatus, currStatus);
			}
			//查看是否可以催缴号码
			
			
//			return super.halfstop(user);

		return 1;
	}	
	/**
	* @Title: checkDelay 
	* @Description: 检查是否存在延迟停机参数
	*    延迟单停
	*    	延时单停
	*    	延款单停
	*    延迟双停 
	*    	延时双停
	*    	延款双停
	*    再次先实现延时双停
	* @param  delaytype   设定文件
	* @return void    返回类型 
	* @throws
	 */
	private Integer getDelaytimes(String delaytype) throws OmcException{
		ConfigContainer cfg = this.getConfig();
		JsonObject data = this.getIndata();
		String policyId = data.get(OmcCalKey.OMC_POLICY_ID).getAsString();
		String delaystop = cfg.getCfgPara(delaytype, this.getOmcobj().getTenantid(), policyId, "0");
		//未配置延迟停机参数为空
		if (StringUtils.isBlank(delaystop)){
			return 0;
		}
		
		String timestype = "";
		//判断延迟停机类型
		if (delaytype.equals(OmcCalKey.OMC_CFG_DELAYSTOP)){
			timestype = OmcCalKey.OMC_CFG_DELAYSTOPTIMES;
		}else if (delaytype.equals(OmcCalKey.OMC_CFG_DELAYHALFSTOP)){
			timestype = OmcCalKey.OMC_CFG_DELAYHALFSTOPTIMES;
		}
		//获取租户允许延迟时间
		String delaystoptimes = cfg.getCfgPara(timestype, this.getOmcobj().getTenantid(), policyId, "0");
        if (StringUtils.isBlank(delaystoptimes)){
        	return 0;
        }
		Integer ndelaystoptimes;
		if (!StringUtils.isNumeric(delaystoptimes)){
			throw new OmcException("OMC_checkStopDelay", "延迟停机时间长度配置错误"+delaystoptimes);
		}else{
			ndelaystoptimes = Integer.parseInt(delaystoptimes);
		}
		
		if ((delaystop.equals(YesNo.YES))&&(ndelaystoptimes<=0)){
			throw new OmcException("OMC_checkStopDelay", "延迟停机时间长度配置错误" + delaystoptimes);
		}

		return ndelaystoptimes;
	}

	private ScoutStatus newScoStatus(User user,String currStatus){
		ScoutStatus scoutStatus = new ScoutStatus();
		scoutStatus.setScoSeq(0L);
		scoutStatus.setAcctId(user.getAccountid());
		scoutStatus.setBusinessCode(this.getOmcobj().getBusinesscode());
		scoutStatus.setCustId(user.getCustomerid());
		scoutStatus.setTenantId(user.getTenantid());
		scoutStatus.setSystemId(user.getSystemid());
		scoutStatus.setSubsId(user.getSubsid());

		scoutStatus.setLastStatus(currStatus);   
		scoutStatus.setStatus(currStatus);
		scoutStatus.setNotifyStatus("0");
		scoutStatus.setNotifyTime(DateUtils.currTimeStamp());
		scoutStatus.setNotifyTimes(0);
		scoutStatus.setNotifyType("0");
		scoutStatus.setScoutInfo("后续处理");
		scoutStatus.setStatusTime(DateUtils.currTimeStamp());
		
		return scoutStatus;
	}
	private ScoutStatus modiScoStatus(ScoutStatus status,String currStatus){
		status.setLastStatus(status.getStatus());   
		status.setStatus(currStatus);
		status.setNotifyTime(DateUtils.currTimeStamp());
		status.setStatusTime(DateUtils.currTimeStamp());
		return status;
	}
}
