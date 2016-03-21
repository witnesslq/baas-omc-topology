package com.ai.baas.omc.topoligy.core.business.command;

import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.constant.SCORULETYPE;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.OmcScoutActionDefine;
import com.ai.baas.omc.topoligy.core.manager.service.ScoutStatusService;
import com.ai.baas.omc.topoligy.core.manager.service.db.ScoutStatusServiceImpl;
import com.ai.baas.omc.topoligy.core.pojo.*;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.google.gson.JsonObject;
/**
 * 
* @ClassName: ScoutActBms 
* @Description: 提供停开机指令生成，停开机前短信发送，停开机预警状态和短信处理功能 
* @author lvsj
* @date 2015年11月12日 下午9:03:50 
*
 */

public class ScoutActBms {
	final private ConfigContainer config;
	final private OmcObj omcobj;
	final private JsonObject indata;
	final private RealTimeBalance realtimeBalance;
	
	protected ScoutStatusService scoutStatusService;
	protected OmcBmsInterface omcBmsInterface = null;
	protected ScoutStatus myscoutStatus = null;
	protected SmsInf smsInfs = null;

	/**
	 * 
	* @Title: stop 
	* @Description: 处理停机接口数据，将停机数据插入到停机队列中 
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public ScoutActBms(OmcObj owner,ConfigContainer cfg,RealTimeBalance balance,JsonObject data){
		super();
		this.config = cfg;
		this.omcobj = owner;
		this.indata = data;
		this.realtimeBalance = balance;
		
		scoutStatusService = new ScoutStatusServiceImpl();
	}

	public int stop(User user) throws OmcException {
		ConfigContainer cfg = this.getConfig();
		OmcObj actionObj = this.getOmcobj();
		
		OmcScoutActionDefine action = cfg.getActionDefine(actionObj.getTenantid(),actionObj.getBusinesscode(), "-1",SCORULETYPE.STOP);
		//添加短信支持
		sendmsg(user, SCORULETYPE.STOP,action.getSmsTemplate());
		//获取停机指令	
		String actionType = action.getInfCommond();
		omcBmsInterface = builderinf(user,SCORULETYPE.STOP,actionType);
		return 1;
	}
	/**
	 * 
	* @Title: start 
	* @Description: 处理开机接口数据，将停机数据插入到开机队列中 
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int start(User user) throws OmcException {
		ConfigContainer cfg = getConfig();
		OmcObj actionObj = getOmcobj();
		//获取指定定义
		OmcScoutActionDefine action = cfg.getActionDefine(actionObj.getTenantid(),actionObj.getBusinesscode(), "-1",SCORULETYPE.START);
		
		//添加短信支持
		sendmsg(user,SCORULETYPE.START,action.getSmsTemplate());
		//获取信控指令	
		String actionType = action.getInfCommond();

		omcBmsInterface = builderinf(user,SCORULETYPE.START,actionType);
		
		return 1;
	}
	
	public int halfstop(User user)  throws OmcException {
		
		ConfigContainer cfg = this.getConfig();
		OmcObj actionObj = this.getOmcobj();
		//获取指定定义
		OmcScoutActionDefine action = cfg.getActionDefine(actionObj.getTenantid(),actionObj.getBusinesscode(), "-1",SCORULETYPE.HALFSTOP);
		
		//添加短信支持
		sendmsg(user,SCORULETYPE.HALFSTOP,action.getSmsTemplate());
		//获取信控指令	
		String actionType = action.getInfCommond();
		
		omcBmsInterface = builderinf(user,SCORULETYPE.HALFSTOP,actionType);
		
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
	protected void sendmsg(User user,String scoutType,String templateID){
		if ((templateID==null)||(templateID.isEmpty())){
			return ;
		}
//		smsInfs = new SmsInf();
//		smsInfs.setCreateTime(DateUtils.currTimeStamp());
//		smsInfs.setCreateTime(DateUtils.currTimeStamp());
//		smsInfs.setFlag(0);
//		smsInfs.setGsmcontent("phone:"+user.getServicenum());
//		smsInfs.setPhone(user.getServicenum());
//		smsInfs.setPriority(0);
//		smsInfs.setServicetype(user.getBasicorgid());
//		smsInfs.setSrcName("CREDIT"+scoutType);
//		smsInfs.setTemplateId(Long.valueOf(templateID));
//		smsInfs.setVerifyid(0l);
		
	}

	private String getbmsRemark(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("实时余额：" + realtimeBalance.getRealBalance());
		stringBuffer.append("预存余额：" + realtimeBalance.getBalance());
		stringBuffer.append("实时话费：" + realtimeBalance.getRealBill());
		stringBuffer.append("历史话费：" + realtimeBalance.getUnSettleBill());
		stringBuffer.append("欠费月数：" + realtimeBalance.getUnsettleMons());
		stringBuffer.append("最早欠费月：" + realtimeBalance.getFstUnSettleMon());
		return stringBuffer.toString();
	}
	private String getbmsData(User user,String scotype){
		String data = "";
		data += "3|" + user.getSubsid();
		data +=  "|" + user.getServicetype();
		data +=  "|" +"A";
		data +=  "|" + scotype;
		data += getbmsRemark();

		return data;
	}
	private String getinfData(User user,String scouttype,String commonid){

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("balance", realtimeBalance.getRealBalance());
		jsonObject.add("pileid", this.getIndata().get(OmcCalKey.OMC_CHARGING_PILE));
		jsonObject.add("statid", this.getIndata().get(OmcCalKey.OMC_CHARGING_STATION));
		jsonObject.addProperty("iccid", user.getServicenum());
		jsonObject.addProperty("subs_id", user.getSubsid());
		jsonObject.addProperty("scout_type", scouttype);
		jsonObject.addProperty("commanid", commonid);
		
		return jsonObject.toString();

	}
	private OmcBmsInterface builderinf(User user,String scouttype,String commonid){
		OmcBmsInterface bmsinf = new OmcBmsInterface();

		
		String bmsdata = getbmsData(user,scouttype);
		String infdata = getinfData(user,SCORULETYPE.HALFSTOP,commonid);
		bmsinf.setSerialNo(0L);  //统一赋值
		bmsinf.setTenantId(user.getTenantid());
		bmsinf.setSystemId(user.getSystemid());
		bmsinf.setAcctId(user.getAccountid());
		bmsinf.setBmsData(bmsdata);
		bmsinf.setDealFlag(0);
		bmsinf.setDealTime(DateUtils.currTimeStamp());
		bmsinf.setInsertTime(DateUtils.currTimeStamp());
		bmsinf.setInterfaceData(infdata);
		bmsinf.setRemark("");
		bmsinf.setRetryTimes(0);
		bmsinf.setScoutType(scouttype);

		bmsinf.setSubsId(user.getSubsid());
		return bmsinf;
	}
	
	public OmcBmsInterface getOmcBmsInterfaces() {
		return omcBmsInterface;
	}
	public void setOmcBmsInterfaces(OmcBmsInterface omcBmsInterfaces) {
		this.omcBmsInterface = omcBmsInterfaces;
	}
	public ScoutStatus getMyscoutStatus() {
		return myscoutStatus;
	}
	public void setMyscoutStatus(ScoutStatus myscoutStatus) {
		this.myscoutStatus = myscoutStatus;
	}
	public SmsInf getSmsInfs() {
		return smsInfs;
	}
	public void setSmsInfs(SmsInf smsInfs) {
		this.smsInfs = smsInfs;
	}

	public OmcObj getOmcobj() {
		return omcobj;
	}

	public ConfigContainer getConfig() {
		return config;
	}
	public JsonObject getIndata() {
		return indata;
	}
	
	public OmcBmsInterface getOmcBmsInterface() {
		return omcBmsInterface;
	}
	public void setOmcBmsInterface(OmcBmsInterface omcBmsInterface) {
		this.omcBmsInterface = omcBmsInterface;
	}
	public RealTimeBalance getRealtimeBalance() {
		return realtimeBalance;
	}
}
