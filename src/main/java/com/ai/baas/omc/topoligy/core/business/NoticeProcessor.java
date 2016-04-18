package com.ai.baas.omc.topoligy.core.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ai.baas.omc.topoligy.core.business.base.BaseProcess;
import com.ai.baas.omc.topoligy.core.business.command.ScoutActBmsExt;
import com.ai.baas.omc.topoligy.core.business.command.ScoutActSmsExt;
import com.ai.baas.omc.topoligy.core.constant.*;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.manager.service.*;
import com.ai.baas.omc.topoligy.core.manager.service.db.*;
import com.ai.baas.omc.topoligy.core.pojo.*;
import com.ai.baas.omc.topoligy.core.util.OmcUtils;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import org.apache.commons.lang.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NoticeProcessor extends BaseProcess {
	private static Logger LOGGER = LoggerFactory.getLogger(NoticeProcessor.class);
	private SpeUrgeStopService speUrgeStopService;
	private ScoutStatusService scoutStatusService;
	private UrgeStatusService urgeStatusService;
	private ScoutBmsInterfaceService scoutBmsInterfaceService;
	private SgipSrcGsmService sgipSrcGsmService;
	private ScoutLogService scoutLogService;
	private static final JdbcProxy DB_PROXY = JdbcProxy.getInstance();
	private InformationProcessor info = null;
	private RealTimeBalance realBalance;
	
	public NoticeProcessor(ConfigContainer cfg, OmcObj obj, JsonObject data, RealTimeBalance balance) throws OmcException {
		super(cfg, obj, data);
		this.realBalance = balance;
		speUrgeStopService = new SpeUrgeStopServiceImpl();
		scoutStatusService = new ScoutStatusServiceImpl();
		urgeStatusService = new UrgeStatusServiceImpl();
		scoutBmsInterfaceService = new ScoutBmsInterfaceServiceImpl();
		sgipSrcGsmService = new SgipSrcGsmServiceImpl();
		scoutLogService = new ScoutLogServiceImpl();
	}

	@Override
	public void process() throws OmcException {
		JsonObject jsonObject = this.getInput();
		//规则id列表
		String rules = jsonObject.get(OmcCalKey.OMC_RULE_ID_LIST).toString();
		//策略id
		String policyId = jsonObject.get(OmcCalKey.OMC_POLICY_ID).getAsString();
		//规则详细信息列表
		List<SectionRule> sectionRules = OmcUtils.toSectionRules(this.getConfig(),rules);
		
		//设置信控对象
		info = new InformationProcessor(this.getConfig(),this.getOmcobj(),jsonObject);
		//获取三户资料
		info.process();
		
		List<User> users = info.getUsers();
		//逐条进行处理 信控操作进行处理
		for (SectionRule rule:sectionRules){
			//时间过滤
			if (!filterByTime(rule)){
				continue;
			}
			//规则类型为stop
			if (rule.getScouttype().equals(ScoRuleType.STOP)) {
				stop(users, rule);
			}else if (rule.getScouttype().equals(ScoRuleType.HALFSTOP)) {
				halfstop(users, rule);
			}else if (rule.getScouttype().equals(ScoRuleType.START)) {
				start(users, rule);
			}else if (rule.getScouttype().equals(ScoRuleType.WARNING)) {
				warning(info, rule, policyId);
			}else if (rule.getScouttype().equals(ScoRuleType.WARNOFF)) {
				warnoff(info, rule, policyId);
			}
		}
		
		//按照用户提醒  是否提醒到其他号码  可以本号码/其他号码    用户级余额  账户级余额  客户级余额
		//按照客户提醒  是否提醒到其他号码  账户提醒 默认提醒到其他号码
		//按照账户提醒  是否提醒到其他号码  客户提醒 默认提醒到其他号码
	}

	/**
	 * @throws OmcException 
	 * 
	* @Title: filterBySpeUrgeStop 
	* @Description: 免催停过滤 
	* @param @param subsid  用户iD
	* @param @param sectionRules 规则类型
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private boolean filterBySpeUrgeStop(String ownertype,String ownerid,SectionRule sectionRules) throws OmcException{
		SpeUrgeStop speUrgeStop = speUrgeStopService.selectById(this.getOmcobj().getTenantid(), ownertype, ownerid);
		boolean ret = true;
		if (speUrgeStop != null){
			if ((speUrgeStop.getSpeType().equals(AvoidType.AVOID_STOP))
					||(speUrgeStop.getSpeType().equals(AvoidType.AVOID_STOPANDURGE))){
				if ((sectionRules.getScouttype().equals(ScoRuleType.HALFSTOP))
						||(sectionRules.getScouttype().equals(ScoRuleType.STOP))){
					ret = false;
				}
			}else if((speUrgeStop.getSpeType().equals(AvoidType.AVOID_URGE))
					||(speUrgeStop.getSpeType().equals(AvoidType.AVOID_STOPANDURGE))){
				if ((sectionRules.getScouttype().equals(ScoRuleType.WARNING))){
					ret = false;
				}
			}
		}
		return ret;
		
	}

	/**
	 * 停机操作
	 * @param users
	 * @param sectionRule
	 * @throws OmcException
     */
	private void stop(List<User> users,SectionRule sectionRule)  throws OmcException {
		List<OmcBmsInterface> omcBmsInterfaces = new ArrayList<OmcBmsInterface>();
		List<SmsInf> smsInfs = new ArrayList<SmsInf>();
		List<ScoutStatus> scoutStatus = new ArrayList<ScoutStatus>();
		List<OmcUrgeStatus> omcUrgeStatus = new ArrayList<OmcUrgeStatus>();
		ScoutActBmsExt scoutActBmsExt = new ScoutActBmsExt(this.getOmcobj(), this.getConfig(), this.realBalance,this.getInput());
		for (User user:users){
			//免催免停处理
			if (!filterBySpeUrgeStop(OwnerType.SERV,user.getSubsid(),sectionRule)){
				continue;
			}
			scoutActBmsExt.stop(user);
			
			if (scoutActBmsExt.getMyscoutStatus()!=null){
				scoutStatus.add(scoutActBmsExt.getMyscoutStatus());
			}
			if (scoutActBmsExt.getOmcBmsInterfaces()!=null){
				omcBmsInterfaces.add(scoutActBmsExt.getOmcBmsInterfaces());
			}
			if (scoutActBmsExt.getSmsInfs()!=null){
				smsInfs.add(scoutActBmsExt.getSmsInfs());
			}
		}
		ScoutLog scoLog = null;
		if ((omcBmsInterfaces!=null)&&(!omcBmsInterfaces.isEmpty())){
			scoLog = new ScoutLog();
			scoLog.setLogid(0L);
			scoLog.setOwner(this.getOmcobj());
			scoLog.setRealTimeBalance(this.getRealBalance());
			scoLog.setScostatus(ScoRuleType.STOP);
			scoLog.setSectionRules(sectionRule);
			scoLog.setSourceType(sectionRule.getScouttype());
		}
	
		
		sendCommon(omcBmsInterfaces,smsInfs,scoutStatus,omcUrgeStatus,scoLog);
		
	}
	private void start(List<User> users,SectionRule sectionRule)  throws OmcException {
		List<OmcBmsInterface> omcBmsInterfaces = new ArrayList<OmcBmsInterface>();
		List<SmsInf> smsInfs = new ArrayList<SmsInf>();
		List<ScoutStatus> scoutStatus = new ArrayList<ScoutStatus>();	
		List<OmcUrgeStatus> omcUrgeStatus = new ArrayList<OmcUrgeStatus>();	
		ScoutActBmsExt scoutActBmsExt = new ScoutActBmsExt(this.getOmcobj(), this.getConfig(), this.realBalance,this.getInput());
		for (User user:users){

			scoutActBmsExt.start(user);
			if (scoutActBmsExt.getMyscoutStatus()!=null){
				scoutStatus.add(scoutActBmsExt.getMyscoutStatus());
			}
			if (scoutActBmsExt.getOmcBmsInterfaces() !=null){
				omcBmsInterfaces.add(scoutActBmsExt.getOmcBmsInterfaces() );
			}
			if (scoutActBmsExt.getSmsInfs()!=null){
				smsInfs.add(scoutActBmsExt.getSmsInfs());
			}
		}
		ScoutLog scoLog = null;
		if ((omcBmsInterfaces!=null)&&(!omcBmsInterfaces.isEmpty())){
			scoLog = new ScoutLog();
			scoLog.setLogid(0L);
			scoLog.setOwner(this.getOmcobj());
			scoLog.setRealTimeBalance(this.getRealBalance());
			scoLog.setScostatus(ScoRuleType.START);
			scoLog.setSectionRules(sectionRule);
			scoLog.setSourceType(sectionRule.getScouttype());
		}
		
		sendCommon(omcBmsInterfaces,smsInfs,scoutStatus,omcUrgeStatus,scoLog);
	}	
	private void halfstop(List<User> users,SectionRule sectionRule)  throws OmcException {
		List<OmcBmsInterface> omcBmsInterfaces = new ArrayList<OmcBmsInterface>();
		List<SmsInf> smsInfs = new ArrayList<SmsInf>();
		List<ScoutStatus> scoutStatus = new ArrayList<ScoutStatus>();
		List<OmcUrgeStatus> omcUrgeStatus = new ArrayList<OmcUrgeStatus>();
		ScoutActBmsExt scoutActBmsExt = new ScoutActBmsExt(this.getOmcobj(), this.getConfig(), this.realBalance,this.getInput());
		for (User user:users){
			//免催免停处理
			if (!filterBySpeUrgeStop(OwnerType.SERV,user.getSubsid(),sectionRule)){
				continue;
			}
			scoutActBmsExt.halfstop(user);
			
			if (scoutActBmsExt.getMyscoutStatus()!=null){
				scoutStatus.add(scoutActBmsExt.getMyscoutStatus());
			}
			if (scoutActBmsExt.getOmcBmsInterfaces()!=null){
				omcBmsInterfaces.add(scoutActBmsExt.getOmcBmsInterfaces());
			}
			if (scoutActBmsExt.getSmsInfs()!=null){
				smsInfs.add(scoutActBmsExt.getSmsInfs());
			}
			
		}	
		
		ScoutLog scoLog = null;
		if ((omcBmsInterfaces!=null)&&(!omcBmsInterfaces.isEmpty())){
			scoLog = new ScoutLog();
			scoLog.setLogid(0L);
			scoLog.setOwner(this.getOmcobj());
			scoLog.setRealTimeBalance(this.getRealBalance());
			scoLog.setScostatus(ScoRuleType.HALFSTOP);
			scoLog.setSectionRules(sectionRule);
			scoLog.setSourceType(sectionRule.getScouttype());
		}
	
		sendCommon(omcBmsInterfaces,smsInfs,scoutStatus,omcUrgeStatus,scoLog);
	}	
	private void warning(InformationProcessor info, SectionRule sectionRule, String policyid)  throws OmcException {
		List<OmcBmsInterface> omcBmsInterfaces = new ArrayList<OmcBmsInterface>();
		List<SmsInf> smsInfs = new ArrayList<SmsInf>();
		List<ScoutStatus> scoutStatus = new ArrayList<ScoutStatus>();
		List<OmcUrgeStatus> omcUrgeStatus = new ArrayList<OmcUrgeStatus>();
		
		ConfigContainer cfg = this.getConfig();
		
		String remindTarget = cfg.getCfgPara(OmcCalKey.OMC_CFG_REMINDTARGET,this.getOmcobj().getTenantid(), policyid,Integer.toString(sectionRule.getScoutruleid()));
		
		//缺省配置
		if (StringUtils.isBlank(remindTarget)){
			remindTarget = RemindTarget.TOSERV;
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(OmcCalKey.OMC_RULE_ID, sectionRule.getScoutruleid());
		jsonObject.addProperty(OmcCalKey.OMC_RULE_SECTION, sectionRule.getSectiontype());
		jsonObject.addProperty(OmcCalKey.OMC_POLICY_ID, policyid);
		ScoutActSmsExt scoutActSmsExt = new ScoutActSmsExt(this.getOmcobj(),info,this.getConfig(),this.getRealBalance(),jsonObject);
		//根据不同的配置进行处理
        if (remindTarget.equals(RemindTarget.TOSERV)){
        	List<User> remindusers = info.getUsers();
    		for (User user:remindusers){
    			//免催免停处理
    			if (!filterBySpeUrgeStop(OwnerType.SERV,user.getSubsid(),sectionRule)){
    				continue;
    			}
    			
    			scoutActSmsExt.warning(OwnerType.SERV,user.getSubsid());
    			
    			if (scoutActSmsExt.getMyomcUrgeStatus()!=null){
    				omcUrgeStatus.add(scoutActSmsExt.getMyomcUrgeStatus());
    			}
    			if (scoutActSmsExt.getSmsInfs()!=null){
    				smsInfs.add(scoutActSmsExt.getSmsInfs());
    			}
    		}
        	
        }else if(remindTarget.equals(RemindTarget.TOACCT)){
        	List<Account> accounts = info.getAccounts();
    		for (Account account:accounts){
    			//免催免停处理
    			if (!filterBySpeUrgeStop(OwnerType.ACCT,account.getAccountId(),sectionRule)){
    				continue;
    			}	
    			scoutActSmsExt.warning(OwnerType.ACCT,account.getAccountId());
    			
    			if (scoutActSmsExt.getMyomcUrgeStatus()!=null){
    				omcUrgeStatus.add(scoutActSmsExt.getMyomcUrgeStatus());
    			}
    			if (scoutActSmsExt.getSmsInfs()!=null){
    				smsInfs.add(scoutActSmsExt.getSmsInfs());
    			}
    		}
        	
        }else if(remindTarget.equals(RemindTarget.TOCUST)){
        	Customer customer = info.getCustomer();

			//免催免停处理
			if (!filterBySpeUrgeStop(OwnerType.CUST,customer.getCustomerId(),sectionRule)){
				return;
			}
			
			scoutActSmsExt.warning(OwnerType.CUST,customer.getCustomerId());
			if (scoutActSmsExt.getMyomcUrgeStatus()!=null){
				omcUrgeStatus.add(scoutActSmsExt.getMyomcUrgeStatus());
			}
			if (scoutActSmsExt.getSmsInfs()!=null){
				smsInfs.add(scoutActSmsExt.getSmsInfs());
			}
        	
        }
		ScoutLog scoLog = null;
		if ((smsInfs!=null)&&(!smsInfs.isEmpty())){
			scoLog = new ScoutLog();
			scoLog.setLogid(0L);
			scoLog.setOwner(this.getOmcobj());
			scoLog.setRealTimeBalance(this.getRealBalance());
			scoLog.setScostatus(ScoRuleType.WARNING);
			scoLog.setSectionRules(sectionRule);
			scoLog.setSourceType(sectionRule.getScouttype());
		}
  
		sendCommon(omcBmsInterfaces,smsInfs,scoutStatus,omcUrgeStatus,scoLog);
	}
	
	private void warnoff(InformationProcessor info, SectionRule sectionRule, String policyid)  throws OmcException {
		List<OmcBmsInterface> omcBmsInterfaces = new ArrayList<OmcBmsInterface>();
		List<SmsInf> smsInfs = new ArrayList<SmsInf>();
		List<ScoutStatus> scoutStatus = new ArrayList<ScoutStatus>();
		List<OmcUrgeStatus> omcUrgeStatus = new ArrayList<OmcUrgeStatus>();
		
		ConfigContainer cfg = this.getConfig();
		
		String remindTarget = cfg.getCfgPara(OmcCalKey.OMC_CFG_REMINDTARGET,this.getOmcobj().getTenantid(), policyid,Integer.toString(sectionRule.getScoutruleid()));
		
		//缺省配置 ,默认提醒到用户
		if (StringUtils.isBlank(remindTarget)){
			remindTarget = RemindTarget.TOSERV;
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(OmcCalKey.OMC_RULE_ID, sectionRule.getScoutruleid());
		jsonObject.addProperty(OmcCalKey.OMC_RULE_SECTION, sectionRule.getSectiontype());
		jsonObject.addProperty(OmcCalKey.OMC_POLICY_ID, policyid);
		ScoutActSmsExt scoutActSmsExt = new ScoutActSmsExt(
				this.getOmcobj(),info,this.getConfig(),this.getRealBalance(),jsonObject);
		//根据不同的配置进行处理
		//用户
        if (remindTarget.equals(RemindTarget.TOSERV)){
        	List<User> remindusers = info.getUsers();
    		for (User user:remindusers){
    			//免催免停处理
    			if (!filterBySpeUrgeStop(OwnerType.SERV,user.getSubsid(),sectionRule)){
    				continue;
    			}	
    			
    			scoutActSmsExt.warnoff(OwnerType.SERV,user.getSubsid());
    			
    			if (scoutActSmsExt.getMyomcUrgeStatus()!=null){
    				omcUrgeStatus.add(scoutActSmsExt.getMyomcUrgeStatus());
    			}
    		}
        //账户
        }else if(remindTarget.equals(RemindTarget.TOACCT)){
        	List<Account> accounts = info.getAccounts();
    		for (Account account:accounts){
    			//免催免停处理
    			if (!filterBySpeUrgeStop(OwnerType.ACCT,account.getAccountId(),sectionRule)){
    				continue;
    			}	
    			scoutActSmsExt.warnoff(OwnerType.ACCT,account.getAccountId());
    			
    			if (scoutActSmsExt.getMyomcUrgeStatus()!=null){
    				omcUrgeStatus.add(scoutActSmsExt.getMyomcUrgeStatus());
    			}
    		}
        //客户
        }else if(remindTarget.equals(RemindTarget.TOCUST)){
        	Customer customer = info.getCustomer();

			//免催免停处理
			if (!filterBySpeUrgeStop(OwnerType.CUST,customer.getCustomerId(),sectionRule)){
				return;
			}
			
			scoutActSmsExt.warnoff(OwnerType.CUST,customer.getCustomerId());
			if (scoutActSmsExt.getMyomcUrgeStatus()!=null){
				omcUrgeStatus.add(scoutActSmsExt.getMyomcUrgeStatus());
			}
        }
        
		ScoutLog scoLog = null;
		if ((omcUrgeStatus!=null)&&(!omcUrgeStatus.isEmpty())){
			scoLog = new ScoutLog();
			scoLog.setLogid(0L);
			scoLog.setOwner(this.getOmcobj());
			scoLog.setRealTimeBalance(this.getRealBalance());
			scoLog.setScostatus(ScoRuleType.WARNOFF);
			scoLog.setSectionRules(sectionRule);
			scoLog.setSourceType(sectionRule.getScouttype());
		}
  
		sendCommon(omcBmsInterfaces,smsInfs,scoutStatus,omcUrgeStatus,scoLog);
	}
	/**
	 * 
	* @Title: filterByTime 
	* @Description: 按照时间段进行过滤，在指定时间段内不做对应信控动作 
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private boolean filterByTime(SectionRule sectionRule){
		//Todo  待完成
		return true;
	}

	private void sendCommon(
			List<OmcBmsInterface> bmsinfs,List<SmsInf> smsinfs,List<ScoutStatus> scoutStatus,
			List<OmcUrgeStatus> omcurgeStatus,ScoutLog scoutLog) throws OmcException{
			Connection connection = DB_PROXY.getConnection();
			
			try {
				if (connection.getAutoCommit())
					connection.setAutoCommit(false);

				//保存状态表
				this.sendScoStatus(connection, scoutStatus);
				//保存停开机接口表
				this.sendBmsInterface(connection, bmsinfs);
				//保存短信通知表
				this.sendSmsInterface(connection, smsinfs);
				//保存催缴表
				this.sendUrgeStatus(connection, omcurgeStatus);
				//保存日志表
				this.sendScoLog(connection, scoutLog);

				connection.commit();
			} catch (Exception e) {
				LOGGER.error("信控结果保存异常",e);
				try {
					connection.rollback();
					throw new OmcException("sendCommon","信控结果保存异常",e);
				} catch (Exception e1) {
					throw new OmcException("sendCommon","信控结果保存时发生数据库异常",e);
				}
			} finally {
				try {
					connection.setAutoCommit(true);
					connection.close();
				} catch (SQLException e) {
					LOGGER.error("",e);
				}
			}
	}


	/**
	 * 发送停开机信息
	 * @param connection
	 * @param bmsinfs
	 * @throws OmcException
     */
	private void sendBmsInterface(Connection connection,List<OmcBmsInterface> bmsinfs) throws OmcException{
		if ((bmsinfs == null)||(bmsinfs.isEmpty())){
			return;
		}
		
		String breakpoint = "sendCommon.bmsinfs";
		
		for (OmcBmsInterface inf:bmsinfs){
			inf.setSerialNo(SysSequence.getInstance().getSequence("SCOUT_BMS_INTERFACE_SEQ"));
			if (scoutBmsInterfaceService.addInterFace(connection,inf) <= 0){
				throw new OmcException(breakpoint, "更新停开机接口表异常");							
			}
		}
	}

	/**
	 * 发送短信通知
	 * @param connection
	 * @param smsinfs
	 * @throws OmcException
     */
	private void sendSmsInterface(Connection connection,List<SmsInf> smsinfs) throws OmcException{
		if ((smsinfs == null)||(smsinfs.isEmpty())){
			return;
		}
		
		String breakpoint = "sendCommon.smsinfs";
		
		for (SmsInf inf:smsinfs){
			if (inf.getSerialno()==0L){
				inf.setSerialno(SysSequence.getInstance().getSequence("SGIP_SRC_GSM_SEQ"));
			}

			if ( sgipSrcGsmService.insertMsg(connection,inf) <= 0){
				throw new OmcException(breakpoint, "更新短信接口表异常");
			}
		}
	}

	/**
	 * 更新催缴状态
	 * @param connection
	 * @param omcurgeStatus
	 * @throws OmcException
     */
	private void sendUrgeStatus(Connection connection,List<OmcUrgeStatus> omcurgeStatus) throws OmcException{
		if ((omcurgeStatus == null)||(omcurgeStatus.isEmpty())){
			return;
		}
		
		String breakpoint = "sendCommon.omcurgeStatus";
		for (OmcUrgeStatus status:omcurgeStatus){
		    if (status.getUrgeSerial()==0L){
		    	status.setUrgeSerial(SysSequence.getInstance().getSequence("SCOSTATUS_SEQ"));
		    }

			if (urgeStatusService.modifyUrgeStatus(connection,status) <= 0){
				throw new OmcException(breakpoint, "更新预警状态表异常");
			}
		}
	}

	/**
	 * 发送信控状态
	 * @param connection
	 * @param scoutStatus
	 * @throws OmcException
     */
	private void sendScoStatus(Connection connection,List<ScoutStatus> scoutStatus) throws OmcException{
		if ((scoutStatus == null)||(scoutStatus.isEmpty())){
			return;
		}
		
		String breakpoint = "sendCommon.savestatus";

		for (ScoutStatus status:scoutStatus){
			    if (status.getScoSeq()==0L){
			    	status.setScoSeq(SysSequence.getInstance().getSequence("SCOSTATUS_SEQ"));
			    }
				//更新信控状态
				if (scoutStatusService.modifyScoutStatus(connection,status) <=0 ){
					throw new OmcException(breakpoint, "更新状态异常");
				}
		}
	}

	/**
	 * 发送信控日志
	 * @param connection
	 * @param scoutLog
	 * @throws OmcException
     */
	private void sendScoLog(Connection connection,ScoutLog scoutLog) throws OmcException{
		if ((scoutLog == null)){
			return;
		}
		scoutLog.setLogid(SysSequence.getInstance().getSequence("SCO_SQUENCE"));
		scoutLog.setScostatus("1");
		if (scoutLogService.insertScoutLog(connection,scoutLog) <= 0){
			throw new OmcException("sendCommon.omcurgeStatus", "更新信控日志表异常");
		}
	}
	
	@Override
	public void prepare(JsonObject data) throws OmcException {
		// TODO Auto-generated method stub
	}

	public RealTimeBalance getRealBalance() {
		return realBalance;
	}

	public void setRealBalance(RealTimeBalance realBalance) {
		this.realBalance = realBalance;
	}

	@Override
	public void prepare(String cfg) throws OmcException {
		// TODO Auto-generated method stub
	}

}
