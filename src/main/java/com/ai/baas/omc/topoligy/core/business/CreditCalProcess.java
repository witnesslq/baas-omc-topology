package com.ai.baas.omc.topoligy.core.business;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ai.baas.omc.topoligy.core.business.base.BaseCalProcess;
import com.ai.baas.omc.topoligy.core.constant.BalancecalModel;
import com.ai.baas.omc.topoligy.core.constant.FeeSource;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.constant.ResourceType;
import com.ai.baas.omc.topoligy.core.constant.rule.CreditLineCalModel;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.manager.service.OmcCreditService;
import com.ai.baas.omc.topoligy.core.manager.service.db.OmcCreditServiceImpl;
import com.ai.baas.omc.topoligy.core.pojo.OmcCredit;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.User;
import com.ai.baas.omc.topoligy.core.util.Cal;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import com.google.gson.JsonObject;

public final class CreditCalProcess extends BaseCalProcess {
	
	private OmcCreditService omcCreditService;
	private BigDecimal creditline;
	
	public CreditCalProcess(ConfigContainer cfg, InformationProcessor info, JsonObject data) {
		super(cfg, info, data);
		omcCreditService = new OmcCreditServiceImpl();
	}

	@Override
	public void process() throws OmcException {
		ConfigContainer cfg = this.getConfig();
		JsonObject inputData = this.getInput();

		OmcObj omcObj = this.getInformation().getOmcobj();
		String tenantId = omcObj.getTenantid();
		String policyId = inputData.get(OmcCalKey.OMC_POLICY_ID).getAsString();
		
		//信用度计算模式
		String  creditcalmodel =  cfg.getCfgPara(OmcCalKey.OMC_CFG_CREDITLINE_CALMODEL, tenantId, policyId,"");
		if (StringUtils.isBlank(creditcalmodel)){
			 throw new OmcException("BalanceCal",
					 "获取信用度计算参数【" + OmcCalKey.OMC_CFG_CREDITLINE_CALMODEL + "】失败，请检查配置或者设置缺省值:"
							 + msgInfo(policyId,tenantId,omcObj.getOwertype(),omcObj.getOwerid(),omcObj.getBusinesscode()));
		}
		
		//余额计算模式
		String  balcalmodel =  cfg.getCfgPara(OmcCalKey.OMC_CFG_BALANCECALMODEL, tenantId, policyId,"");
		if (StringUtils.isBlank(balcalmodel)){
			 throw new OmcException("BalanceCal",
					 "获取信用度计算参数【" + OmcCalKey.OMC_CFG_BALANCECALMODEL + "】失败，请检查配置或者设置缺省值:"
							 + msgInfo(policyId,tenantId,omcObj.getOwertype(),omcObj.getOwerid(),omcObj.getBusinesscode()));
		}
		//默认信用度为0
		double creditLine = 0.0;
        //用户信用度计算模式,并且是账户余额计算模式
		if ((creditcalmodel.equals(CreditLineCalModel.SUM_USER))
				&&(balcalmodel.equals(BalancecalModel.ACCTMODEL))){
            //获取用户总的信用度
			creditLine = getUserSum(this.getInformation().getUsers(), ResourceType.CASH);
			this.setCreditline(Cal.bigDecimalFromDouble(creditLine, FeeSource.FROM_CREDIT));
		}else{ 
			//todo待处理
			this.setCreditline(Cal.bigDecimalFromDouble(creditLine, FeeSource.FROM_CREDIT));
			//throw new OmcException("CreditCalProcess", "有待实现的模式" + OmcCalKey.OMC_CFG_CREDITLINE_CALMODEL + "[" + creditcalmodel +"]"+ OmcCalKey.OMC_CFG_BALANCECALMODEL +"["+ balcalmodel);
		}
	}

    /**
     * 获取用户总的信用度
     * @param users 用户集合
     * @param resourceCode 业务类型
     * @return
     * @throws OmcException
     */
	private double getUserSum(List<User> users, String resourceCode) throws OmcException {
		double creditLine = 0.0;
		if (users==null || users.isEmpty())
			return creditLine;
		//对每个用户进行计算
		for (User user:users) {
			List<OmcCredit> omcCredits = getCreditFromDB(user, resourceCode);
			//如果信用度为空,则直接进行下一用户查询
			if (omcCredits == null || omcCredits.isEmpty())
				continue;

			for (OmcCredit omcCredit:omcCredits){
				creditLine += omcCredit.getCreditline();
			}
		}
		return creditLine;
	}

	/**
	 * 从数据库中获取用户的信用度
	 * @param user 用户信息
	 * @param resourcecode 业务类型
	 * @return
	 * @throws OmcException
     */
	private List<OmcCredit> getCreditFromDB(User user,String resourcecode) throws OmcException{
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
		// TODO Auto-generated method stub
	}

	@Override
	public void prepare(String cfg) throws OmcException {
		// TODO Auto-generated method stub
	}

	private String msgInfo(String policyid,String tenantid,String ownertype,String ownerid,String busicode){
		JsonObject messageinfo = new  JsonObject();
		messageinfo.addProperty(OmcCalKey.OMC_POLICY_ID, policyid);
		messageinfo.addProperty(OmcCalKey.OMC_TENANT_ID, tenantid);
		messageinfo.addProperty(OmcCalKey.OMC_OWNER_TYPE, ownertype);
		messageinfo.addProperty(OmcCalKey.OMC_OWNER_ID, ownerid);
		messageinfo.addProperty(OmcCalKey.OMC_BUSINESS_CODE, busicode);
		return messageinfo.toString();
	}
}
