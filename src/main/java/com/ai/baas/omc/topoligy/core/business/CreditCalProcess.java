package com.ai.baas.omc.topoligy.core.business;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ai.baas.omc.topoligy.core.business.base.BaseCalProcess;
import com.ai.baas.omc.topoligy.core.constant.BalancecalModel;
import com.ai.baas.omc.topoligy.core.constant.FEESOURCE;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.constant.RESOURCETYPE;
import com.ai.baas.omc.topoligy.core.constant.rule.CREDITLINECALMODEL;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.manager.service.OmcCreditService;
import com.ai.baas.omc.topoligy.core.manager.service.db.OmcCreditServiceImpl;
import com.ai.baas.omc.topoligy.core.pojo.OmcCredit;
import com.ai.baas.omc.topoligy.core.pojo.User;
import com.ai.baas.omc.topoligy.core.util.Cal;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import com.google.gson.JsonObject;

public final class CreditCalProcess extends BaseCalProcess {
	
	private OmcCreditService omcCreditService;
	private BigDecimal creditline;
	
	public CreditCalProcess(ConfigContainer cfg, InfomationProcessor info, JsonObject data) {
		super(cfg, info, data);
	}

	@Override
	public void process() throws OmcException {
		ConfigContainer cfg = this.getConfig();
		
		JsonObject inputData = this.getInput();
		InfomationProcessor info = this.getInformation();
		
		String ownertype = info.getOmcobj().getOwertype();
		String ownerid = info.getOmcobj().getOwerid();
		String tenantid = info.getOmcobj().getTenantid();
		String busicode = info.getOmcobj().getBusinesscode();
		String policyid = inputData.get(OmcCalKey.OMC_POLICY_ID).getAsString();
		
		//信用度计算模式
		String  creditcalmodel =  cfg.getCfgPara(OmcCalKey.OMC_CFG_CREDITLINE_CALMODEL, tenantid, policyid,"");
		if (StringUtils.isBlank(creditcalmodel)){
			 JsonObject messageinfo = new  JsonObject();
			messageinfo.addProperty(OmcCalKey.OMC_POLICY_ID, policyid);
			messageinfo.addProperty(OmcCalKey.OMC_TENANT_ID, tenantid);
			messageinfo.addProperty(OmcCalKey.OMC_OWNER_TYPE, ownertype);
			messageinfo.addProperty(OmcCalKey.OMC_OWNER_ID, ownerid);
			messageinfo.addProperty(OmcCalKey.OMC_BUSINESS_CODE, busicode);
			 throw new OmcException("BalanceCal", "获取信用度计算参数【" + OmcCalKey.OMC_CFG_CREDITLINE_CALMODEL + "】失败，请检查配置或者设置缺省值:" + messageinfo.toString());
		}
		
		//余额计算模式
		String  balcalmodel =  cfg.getCfgPara(OmcCalKey.OMC_CFG_BALANCECALMODEL, tenantid, policyid,"");
		if (StringUtils.isBlank(balcalmodel)){
			 JsonObject messageinfo = new  JsonObject();
			messageinfo.addProperty(OmcCalKey.OMC_POLICY_ID, policyid);
			messageinfo.addProperty(OmcCalKey.OMC_TENANT_ID, tenantid);
			messageinfo.addProperty(OmcCalKey.OMC_OWNER_TYPE, ownertype);
			messageinfo.addProperty(OmcCalKey.OMC_OWNER_ID, ownerid);
			messageinfo.addProperty(OmcCalKey.OMC_BUSINESS_CODE, busicode);
			 throw new OmcException("BalanceCal", "获取信用度计算参数【" + OmcCalKey.OMC_CFG_BALANCECALMODEL + "】失败，请检查配置或者设置缺省值:" + messageinfo.toString());
		}

        //用户模式计算信用度,并且是账户余额模式
		if ((creditcalmodel.equals(CREDITLINECALMODEL.SUM_USER))
				&&(balcalmodel.equals(BalancecalModel.ACCTMODEL))){
            //获取金额业务类型的信用度
			double creditline = getUserSum(info.getUsers(), RESOURCETYPE.CASH);
			this.setCreditline(Cal.bigDecimalFromDouble(creditline, FEESOURCE.FROMCREDIT));
		}else{ 
			//todo待处理
			double creditline = 0.0;
			this.setCreditline(Cal.bigDecimalFromDouble(creditline, FEESOURCE.FROMCREDIT));
			//throw new OmcException("CreditCalProcess", "有待实现的模式" + OmcCalKey.OMC_CFG_CREDITLINE_CALMODEL + "[" + creditcalmodel +"]"+ OmcCalKey.OMC_CFG_BALANCECALMODEL +"["+ balcalmodel);
		}
	}

    /**
     * 获取用户总的信用度
     * @param users 用户集合
     * @param resourcecode 业务类型
     * @return
     * @throws OmcException
     */
	private double getUserSum(List<User> users, String resourcecode) throws OmcException{
		double creditline = 0.0;
		//对每个用户进行计算
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			 List<OmcCredit> omcCredits = getfromdb(user,resourcecode);
			 if ((omcCredits!=null)&&(!omcCredits.isEmpty())){
				 for (Iterator<OmcCredit> iter = omcCredits.iterator(); iter.hasNext();) {
					OmcCredit omcCredit = (OmcCredit) iter.next();
					creditline = creditline + omcCredit.getCreditline();
				}
			 }
		}	
		return creditline;
	}

	/**
	 * 从数据库中获取用户的信用度
	 * @param user 用户信息
	 * @param resourcecode 业务类型
	 * @return
	 * @throws OmcException
     */
	private List<OmcCredit> getfromdb(User user,String resourcecode) throws OmcException{
		//获取用户信用度 subs 用户 acct 账户  cust 客户
		List<OmcCredit> omcCredits = omcCreditService.getAllCredit(user.getTenantid(),"SUBS", user.getSubsid(),resourcecode);
		if ((omcCredits==null)||(omcCredits.isEmpty())){
			return Collections.emptyList();
		}
		
		for (Iterator<OmcCredit> iterator = omcCredits.iterator(); iterator.hasNext();) {
			OmcCredit omcCredit = iterator.next();
			Date date = DateUtils.getNowDate();
			if (date.after(omcCredit.getExpDate())){
				iterator.remove();
			}
			
			if (date.before(omcCredit.getEffDate())){
				iterator.remove();
			}
		}
		return omcCredits;
	}

	public BigDecimal getCreditline() {

			return (creditline == null)?new BigDecimal("0.00"):creditline;
	}

	public void setCreditline(BigDecimal creditline) {
		this.creditline = creditline;
	}

	@Override
	public void prepare(JsonObject data) throws OmcException {
		omcCreditService = new OmcCreditServiceImpl();
		
	}

	@Override
	public void prepare(String cfg) throws OmcException {
		// TODO Auto-generated method stub
		
	}
		
	
}
