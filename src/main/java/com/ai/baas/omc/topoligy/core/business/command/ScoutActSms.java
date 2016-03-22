package com.ai.baas.omc.topoligy.core.business.command;

import com.ai.baas.omc.topoligy.core.business.InformationProcessor;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.constant.ScoRuleType;
import com.ai.baas.omc.topoligy.core.constant.rule.RemindSpend;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.OmcScoutActionDefine;
import com.ai.baas.omc.topoligy.core.manager.service.ScoutStatusService;
import com.ai.baas.omc.topoligy.core.manager.service.UrgeStatusService;
import com.ai.baas.omc.topoligy.core.manager.service.db.ScoutStatusServiceImpl;
import com.ai.baas.omc.topoligy.core.manager.service.db.UrgeStatusServiceImpl;
import com.ai.baas.omc.topoligy.core.pojo.*;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonObject;
/**
 * 
* @ClassName: ScoutActBms 
* @Description: 提供预警短信发送 
* @author lvsj
* @date 2015年11月12日 下午9:03:50 
*
 */

public class ScoutActSms {
	protected static final Logger logger = LoggerFactory.getLogger(ScoutActBms.class);
	protected ScoutStatusService scoutStatusService;
	protected UrgeStatusService urgeStatusService;
	protected OmcUrgeStatus myomcUrgeStatus = null;
	protected SmsInf smsInfs = null;
	
	final private SectionRule sectionRule ;
	final private String policyid ;
	final private ConfigContainer config;
	final private OmcObj omcobj;
	final private JsonObject indata;
	final private InformationProcessor infomation;
	final private RealTimeBalance realtimeBalance;
	/**
	 * 
	* @Title: stop 
	* @Description: 处理停机接口数据，将停机数据插入到停机队列中 
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public ScoutActSms(OmcObj owner, InformationProcessor info, ConfigContainer cfg, RealTimeBalance balance, JsonObject data){
		super();
		this.config = cfg;
		this.omcobj = owner;
		this.indata = data;
		this.infomation = info;
		this.realtimeBalance = balance;
		policyid = data.get(OmcCalKey.OMC_POLICY_ID).getAsString();
		String ruleid = data.get(OmcCalKey.OMC_RULE_ID).toString();
		this.sectionRule = cfg.getSectionRule(Integer.parseInt(ruleid));

		scoutStatusService = new ScoutStatusServiceImpl();
		urgeStatusService = new UrgeStatusServiceImpl();

	}

	public int warning(String ownertype,String oid)  throws OmcException {

		OmcScoutActionDefine omcScoutActionDefine = config.getActionDefine(this.getOmcobj().getTenantid(),
				this.getOmcobj().getBusinesscode(),Integer.toString(this.sectionRule.getScoutruleid()) , ScoRuleType.WARNING);
		 
      
		String speremind = this.getConfig().getCfgPara(OmcCalKey.OMC_CFG_REMINDSPENBR, omcobj.getTenantid(), policyid, Integer.toString(sectionRule.getScoutruleid()));
		if (StringUtils.isBlank(speremind)){
			speremind = RemindSpend.NOSPENBR;
		}
		
		//提醒到本用户号码
		String remindnbr = this.getInfomation().getRemindNbr(speremind,omcobj.getTenantid(), ownertype, oid);
		if (StringUtils.isBlank(remindnbr)){
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(OmcCalKey.OMC_CFG_REMINDSPENBR, remindnbr);
			jsonObject.addProperty(OmcCalKey.OMC_TENANT_ID, omcobj.getTenantid());
			jsonObject.addProperty(OmcCalKey.OMC_OWNER_TYPE, ownertype);
			jsonObject.addProperty(OmcCalKey.OMC_OWNER_ID, oid);
			
			throw new OmcException("ScoutActSms.warning", "获取提醒号码异常" + jsonObject.toString());
		}
		Sendmsg(remindnbr, ScoRuleType.WARNING,omcScoutActionDefine.getInfCommond(),omcScoutActionDefine.getSmsTemplate());
		return 1;
	}
	
	public int warnoff(String ownertype,String oid)  throws OmcException { 	
		return 1;
	}
	/**
	 * 
	* @Title: Sendmsg 
	* @Description:  停开机前发送短信
	* @param @param user
	* @param @param scoutType
	* @param @param templateID    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected void Sendmsg(String nbr,String scoutType,String commid,String templateid){
		if ((templateid==null)||(templateid.isEmpty())){
			return ;
		}
		RealTimeBalance realtimeBalance = this.getRealtimeBalance();
		SectionRule sectionRule = this.getSectionRule();
		JsonObject jsonObject = new JsonObject();

		OmcObj obj = this.getOmcobj();
		
		jsonObject.addProperty("template_id", templateid);
		jsonObject.addProperty("phone", nbr);
		jsonObject.addProperty("iccid", nbr);
		jsonObject.addProperty("subs_id", obj.getOwerid());
		jsonObject.addProperty("current_value", realtimeBalance.getRealBalance());
		jsonObject.addProperty("limit_value", sectionRule.getBalanceceil());
		jsonObject.addProperty("limit_type", sectionRule.getScouttype());
		jsonObject.addProperty("scout_type", scoutType);
		jsonObject.addProperty("commanid", commid);
		
		
		smsInfs = new SmsInf();
		smsInfs.setDealflag(0);
		smsInfs.setDealtime(DateUtils.currTimeStamp());
		smsInfs.setInserttime(DateUtils.currTimeStamp());
		smsInfs.setOwnerid(obj.getOwerid());
		smsInfs.setOwnertype(obj.getOwertype());
		smsInfs.setRemark("");
		smsInfs.setRetrytime(0);
		smsInfs.setSerialno(0L);
		smsInfs.setSystemid("SYSTEMID");
		smsInfs.setTenantid(obj.getTenantid());
		smsInfs.setUrgeinfo(jsonObject.toString());
		
	}
	

	public SmsInf getSmsInfs() {
		return smsInfs;
	}
	public void setSmsInfs(SmsInf smsInfs) {
		this.smsInfs = smsInfs;
	}

	public OmcUrgeStatus getMyomcUrgeStatus() {
		return myomcUrgeStatus;
	}

	public void setMyomcUrgeStatus(OmcUrgeStatus myomcUrgeStatus) {
		this.myomcUrgeStatus = myomcUrgeStatus;
	}

	public ConfigContainer getConfig() {
		return config;
	}

	public OmcObj getOmcobj() {
		return omcobj;
	}

	public JsonObject getIndata() {
		return indata;
	}

	public RealTimeBalance getRealtimeBalance() {
		return realtimeBalance;
	}

	public InformationProcessor getInfomation() {
		return infomation;
	}

	public SectionRule getSectionRule() {
		return sectionRule;
	}

	public String getPolicyid() {
		return policyid;
	}

}
