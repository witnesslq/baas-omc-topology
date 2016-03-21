package com.ai.baas.omc.topoligy.core.business.command.rule;
 
import com.ai.baas.omc.topoligy.core.constant.BalancecalModel;
import com.ai.baas.omc.topoligy.core.constant.OwnerType;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import org.apache.commons.lang.StringUtils;


public final class ResourceCheck {
	private ResourceCheck(){
		
	}
	public static boolean checkOwnerType(String tenantid,String ownertype,String policyid,ConfigContainer cfg) throws OmcException{
		if (cfg == null){
			throw new OmcException("ResourceCheck", "请配置相应参数");
		}
		
		String  balancemodel = cfg.getCfgPara(OmcCalKey.OMC_CFG_BALANCECALMODEL, tenantid, policyid,"");
		//默认为账户余额模式
		if (StringUtils.isBlank(balancemodel)){
			balancemodel = BalancecalModel.ACCTMODEL;
		}
		
		if ((balancemodel.equals(BalancecalModel.SUBSMODEL))&&((OwnerType.ACCT.equals(ownertype))||(OwnerType.CUST.equals(ownertype)))){
			throw new OmcException("ResourceCheck", "余额模式是用户模式，OWNERTYPE 不能是账户或者客户，请检查参数配置或者输入参数");
		}
		if ((balancemodel.equals(BalancecalModel.ACCTMODEL))&&((OwnerType.CUST.equals(ownertype)))){
			throw new OmcException("ResourceCheck", "余额模式是账户模式，OWNERTYPE 不能是客户");
		}
		return true;	
	}
	
}
