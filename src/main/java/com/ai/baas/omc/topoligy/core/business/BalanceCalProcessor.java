package com.ai.baas.omc.topoligy.core.business;

import java.util.Map;

import com.ai.baas.omc.topoligy.core.business.base.BaseCalProcess;
import com.ai.baas.omc.topoligy.core.constant.BalancecalModel;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.constant.OwnerType;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.RealTimeBalance;
import com.ai.baas.omc.topoligy.core.util.UrlClient;
import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;
/**
 * 余额计算模式
 * @author jackieliu
 *
 */
public final class BalanceCalProcessor extends BaseCalProcess {
	
	private static final long serialVersionUID = 25013473026836549L;
	
	private static final UrlClient urlClient = UrlClient.getInstance();
	
	private RealTimeBalance realBalance = null;

	public BalanceCalProcessor(ConfigContainer cfg, InformationProcessor info, JsonObject data) {
		super(cfg, info, data);
	}

	@Override
	public void process() throws OmcException {
		JsonObject inputData = this.getInput();
		OmcObj omcObj = this.getInformation().getOmcobj();
		String ownertype = omcObj.getOwertype();
		String ownerid = omcObj.getOwerid();
		String tenantid = omcObj.getTenantid();
		String busicode = omcObj.getBusinesscode();
		
		String policyid = inputData.get(OmcCalKey.OMC_POLICY_ID).getAsString();
		String extAmount = inputData.get(OmcCalKey.OMC_EXT_AMOUNT).getAsString();
		
		JsonObject messageinfo = new JsonObject();
		messageinfo.addProperty(OmcCalKey.OMC_POLICY_ID, policyid);
		messageinfo.addProperty(OmcCalKey.OMC_TENANT_ID, tenantid);
		messageinfo.addProperty(OmcCalKey.OMC_OWNER_TYPE, ownertype);
		messageinfo.addProperty(OmcCalKey.OMC_OWNER_ID, ownerid);
		messageinfo.addProperty(OmcCalKey.OMC_BUSINESS_CODE, busicode);
		
		String  balancemodel =  this.getConfig().getCfgPara(OmcCalKey.OMC_CFG_BALANCECALMODEL, tenantid, policyid,"");
		
		if (StringUtils.isBlank(balancemodel)){
			 throw new OmcException("BalanceCal", "获取余额计算参数【" + OmcCalKey.OMC_CFG_BALANCECALMODEL + "】失败，请检查配置或者设置缺省值:" + messageinfo.toString());
		}
		//用户余额模式
		if ((BalancecalModel.SUBSMODEL.equals(balancemodel))&&(OwnerType.SERV.equals(ownertype))){
			builderResBalanceServ(omcObj,extAmount);
		//账户余额模式
		}else if((BalancecalModel.ACCTMODEL.equals(balancemodel))&&(OwnerType.ACCT.equals(ownertype))){
			builderBalanceAcct(omcObj,extAmount);
		//不支持模式
		}else{
			messageinfo.addProperty(OmcCalKey.OMC_CFG_BALANCECALMODEL, balancemodel);
			throw new OmcException("BalanceCal", "获取余额信息:余额模式与OwnerType不一致，请修改配置。" + balancemodel  + messageinfo.toString());
		}
	}
	
	public RealTimeBalance getBalance(){
		return realBalance;
	}

	/**
	 * 用户余额模式
	 * @param owner
	 * @param extinfo
	 * @throws OmcException
	 */
	private void builderResBalanceServ(OmcObj owner, String extinfo) throws OmcException{
		String appname = this.getConfig().getSysconfig().get(OmcCalKey.OMC_CFG_ENVIRONMENT_APP);
		RealTimeBalance realTimeBalance = urlClient.doQuery(appname, owner);
//		RealTimeBalance realTimeBalance = new RealTimeBalance();
//		realTimeBalance.setOwner(owner);
//		realTimeBalance.setAcctMonth("201603");
//		realTimeBalance.setFstUnSettleMon("201603");
//		realTimeBalance.setBalance(new BigDecimal("0"));
//		realTimeBalance.setCreditline(new BigDecimal("0"));
//		realTimeBalance.setExtInfo("{}");
//		realTimeBalance.setRealBalance(new BigDecimal("-1000"));
//		realTimeBalance.setRealBill(new BigDecimal("0"));
//		realTimeBalance.setUnIntoBill(new BigDecimal("0"));
//		realTimeBalance.setUnSettleBill(new BigDecimal("0"));
//		realTimeBalance.setUnsettleMons(0);
		realBalance = realTimeBalance;
	}

	/**
	 * 账户余额模式
	 * @param owner
	 * @param extinfo
	 * @throws OmcException
	 */
	private void builderBalanceAcct(OmcObj owner,String extinfo) throws OmcException{
		Map<String,String> syscfg =  this.getConfig().getSysconfig();
		String appname = syscfg.get(OmcCalKey.OMC_CFG_ENVIRONMENT_APP);
		realBalance = urlClient.doQuery(appname, owner);
	}

	@Override
	public void prepare(JsonObject data) throws OmcException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(String cfg) throws OmcException {
		// TODO Auto-generated method stub
		
	}
	

}
