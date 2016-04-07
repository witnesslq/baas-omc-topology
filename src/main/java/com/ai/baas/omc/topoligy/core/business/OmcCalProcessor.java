package com.ai.baas.omc.topoligy.core.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ai.baas.omc.topoligy.core.business.base.BaseProcess;
import com.ai.baas.omc.topoligy.core.constant.FeeSource;
import com.ai.baas.omc.topoligy.core.constant.rule.*;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.constant.ScoRuleType;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.Customer;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.RealTimeBalance;
import com.ai.baas.omc.topoligy.core.pojo.SectionRule;
import com.ai.baas.omc.topoligy.core.util.Cal;
import com.ai.baas.omc.topoligy.core.util.OmcUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class OmcCalProcessor extends BaseProcess {

	private  static final Logger logger = LoggerFactory.getLogger(OmcCalProcessor.class);	
	private RealTimeBalance realTimeBalance;
	private InformationProcessor info ;
	
	public OmcCalProcessor(ConfigContainer cfg, OmcObj obj, JsonObject data) throws OmcException {
		super(cfg, obj, data);
	}

	@Override
	public void process() throws OmcException {
		JsonObject jsonObject = this.getInput();
//		String extAmount = jsonObject.get(OmcCalKey.OMC_EXT_AMOUNT).getAsString();
		//信控规则
		String rules = jsonObject.get(OmcCalKey.OMC_RULE_ID_LIST).toString();
		//信控策略
		String policyId = jsonObject.get(OmcCalKey.OMC_POLICY_ID).getAsString();
		//信控规则字符串转换为信控规则对象
		List<SectionRule> sectionRules = OmcUtils.toSectionRules(this.getConfig(),rules);
		//准备资料 获取三户信息
		info = new InformationProcessor(this.getConfig(),this.getOmcobj(),jsonObject);
		info.process();
		this.setInfo(info);
		
		//余额计算
		BalanceCalProcessor balanceCalProcessor = new BalanceCalProcessor(this.getConfig(),info,jsonObject);
		balanceCalProcessor.process();
		RealTimeBalance realTimeBalance = balanceCalProcessor.getBalance();

		//获取信用度
		CreditCalProcess creditCalProcess = new CreditCalProcess(this.getConfig(),info,jsonObject);
		creditCalProcess.process();
		realTimeBalance.setCreditline(creditCalProcess.getCreditline());
		//realTimeBalance.setRealBalance(new BigDecimal("10000"));
		this.setRealTimeBalance(realTimeBalance);
		
		List<SectionRule> selectedRules = meetRule(sectionRules,policyId);
		//匹配规则		
		JsonArray jsonArray = new JsonArray();
		if (( selectedRules != null)&&(!selectedRules.isEmpty())){
			for (SectionRule sectionRule:selectedRules) {
				JsonObject obj = new JsonObject();
				obj.addProperty(OmcCalKey.OMC_RULE_ID, sectionRule.getScoutruleid());
				jsonArray.add(obj);
			}
		}
		JsonObject out = new JsonObject();
		out.add(OmcCalKey.OMC_RULE_ID_LIST, jsonArray);
		this.setOutput(out);
	}
	
	/**
	 * @throws OmcException 
	 * 根据资料和余额情况与规则进行匹配
	* @Title: meetRule 
	* @Description: 根据资料和余额情况与规则进行匹配
	* @return void    返回类型 
	* @throws
	 */
	private List<SectionRule>  meetRule(List<SectionRule> matchRule,String policyid) throws OmcException{
		//获取客户信息
		Customer customer =  this.getInfo().getCustomer();
		logger.info("客户信息："+customer.toString());
		//首先资料级别匹配 资料匹配没有优先级，需要全量匹配，现金提供客户等级
		//客户等级匹配的规则
		List<SectionRule> sectionRules = matchCreditLevel(customer,matchRule,policyid);
		if ((sectionRules==null)||(sectionRules.isEmpty())){
			return sectionRules;
		}
		//阀值类匹配，阀值类匹配 包括欠费，高额，余额等，优先级依次降低
		logger.info("余额信息：" + realTimeBalance.toString());
		logger.info("开始匹配规则");
		//首先判断欠费
		//其次判断高额
		//最后判断余额
		return thresholdMatch(realTimeBalance,sectionRules,policyid);
	}
	/**
	 * @throws OmcException 
	 * 
	* @Title: thresholdMatch 
	* @Description: 阀值类匹配  匹配余额，欠费月份，高额等
	* @param @param balance
	* @param @param sectionRules
	* @param @return    设定文件 
	* @return List<SectionRule>    返回类型 
	* @throws
	 */
	private List<SectionRule> thresholdMatch(RealTimeBalance balance,List<SectionRule> sectionRules,String policyid) throws OmcException{
		//欠费天数
		List<SectionRule> matchRules = matchOwners( balance, sectionRules,policyid);
		//若欠费天数匹配规则为空,则进行实时费用匹配规则
		if (matchRules==null || matchRules.isEmpty()){
			matchRules = matchCharge( balance, sectionRules,policyid);
		}
		//若欠费天数匹配规则和费用匹配规则均为空,则进行余额匹配
		if (matchRules==null || matchRules.isEmpty()){
			matchRules = matchBalance( balance, sectionRules,policyid);
		}
		return matchRules;
	}
	
	/**
	 * @throws OmcException 
	 * 
	* @Title: matchbalance 
	* @Description: 匹配余额
	* @param @param balance
	* @param @param sectionRules
	* @param @return    设定文件 
	* @return List<SectionRule>    返回类型 
	* @throws
	 */
	
	private List<SectionRule> matchBalance(RealTimeBalance balance,List<SectionRule> sectionRules,String policyid) throws OmcException{
		List<SectionRule> sRules = new ArrayList<SectionRule>();
		ConfigContainer cfg = this.getConfig();
		String  matchbalance = cfg.getCfgPara(OmcCalKey.OMC_CFG_MATCH_BALANCE,this.getOmcobj().getTenantid(),policyid,"");
		String  stopaddcredit = cfg.getCfgPara(OmcCalKey.OMC_CFG_STOP_ADDCREDITLINE,this.getOmcobj().getTenantid(),policyid,"");
		String  startaddcredit = cfg.getCfgPara(OmcCalKey.OMC_CFG_START_ADDCREDITLINE,this.getOmcobj().getTenantid(),policyid,"");
		String  warnaddcredit = cfg.getCfgPara(OmcCalKey.OMC_CFG_WARN_ADDCREDITLINE,this.getOmcobj().getTenantid(),policyid,"");

		if (MatchBalance.MATCH.equals(matchbalance)){
			for (Iterator<SectionRule> iterator = sectionRules.iterator(); iterator.hasNext();) {
				SectionRule sectionRule = iterator.next();
				//匹配余额
				BigDecimal floor = Cal.bigDecimalFromLong(sectionRule.getBalancefloor(), FeeSource.FROM_NO_SOURCE);
				BigDecimal ceil = Cal.bigDecimalFromLong(sectionRule.getBalanceceil(), FeeSource.FROM_NO_SOURCE);
				//用户余额
				BigDecimal realbalance = balance.getRealBalance();
				//监控规则类型
				//判断是否需要加入信用度
				if (sectionRule.getScouttype().equals(ScoRuleType.HALFSTOP)||
					sectionRule.getScouttype().equals(ScoRuleType.STOP)){
					if (stopaddcredit.equals(YesNo.YES)){
						realbalance = realbalance.add(balance.getCreditline());
					}
				}
				if (sectionRule.getScouttype().equals(ScoRuleType.START)){
					if (startaddcredit.equals(YesNo.YES)){
						realbalance = realbalance.add(balance.getCreditline());
					}
				}
				if (sectionRule.getScouttype().equals(ScoRuleType.WARNING)||
					sectionRule.getScouttype().equals(ScoRuleType.WARNOFF)){
					if (warnaddcredit.equals(YesNo.YES)){
						realbalance = realbalance.add(balance.getCreditline());
					}
				}
			    if ((realbalance.compareTo(floor)>0)&&(realbalance.compareTo(ceil)<=0)){
			    	sRules.add(sectionRule);
			    }
			}
		}
		
		return sRules;
	}
	/**
	 * @throws OmcException 
	 * 
	* @Title: matchcharge 
	* @Description: 匹配账单
	* @param @param curr
	* @param @param ceil
	* @param @param floor
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private List<SectionRule> matchCharge(RealTimeBalance balance,List<SectionRule> sectionRules,String policyid) throws OmcException{
		List<SectionRule> sRules = new ArrayList<SectionRule>();
		ConfigContainer cfg = this.getConfig();
		String  matchcharge = cfg.getCfgPara(OmcCalKey.OMC_CFG_MATCH_CHARGE,this.getOmcobj().getTenantid(),policyid,"");

		if (MatchCharge.MATCH.equals(matchcharge)){
			for (Iterator<SectionRule> iterator = sectionRules.iterator(); iterator.hasNext();) {
				SectionRule sectionRule = (SectionRule) iterator.next();
				//匹配余额 大于费用最小金额,小于费用最大金额
				BigDecimal floor = Cal.bigDecimalFromLong(sectionRule.getChargefloor(), FeeSource.FROM_CREDIT);
				BigDecimal ceil = Cal.bigDecimalFromLong(sectionRule.getChargeceil(), FeeSource.FROM_CREDIT);
			    if ((balance.getRealBill().compareTo(floor)>0)
						&&(balance.getRealBill().compareTo(ceil)<=0)){
			    	sRules.add(sectionRule);
			    }
			}
		}
		
		return sRules;
	}
	/**
	 * @throws OmcException 
	 * 
	* @Title: matchowners 
	* @Description: 欠费类规则匹配
	* @param @param balance
	* @param @param sectionRules
	* @param @return    设定文件 
	* @return List<SectionRule>    返回类型 
	* @throws
	 */
	
	private List<SectionRule> matchOwners(RealTimeBalance balance,List<SectionRule> sectionRules,String policyid) throws OmcException{
		List<SectionRule> sRules = new ArrayList<SectionRule>();
		ConfigContainer cfg = this.getConfig();
		String  matchowners = cfg.getCfgPara(OmcCalKey.OMC_CFG_MATCH_OWNERS,this.getOmcobj().getTenantid(),policyid,"");
		if (MatchOwners.MATCH.equals(matchowners)){
			for (Iterator<SectionRule> iterator = sectionRules.iterator(); iterator.hasNext();) {
				SectionRule sectionRule = iterator.next();
				//匹配余额 大于最小天数,小于最大天数
			    if ((balance.getUnSettleMons()>sectionRule.getOwemindays())
						&&(balance.getUnSettleMons()<=sectionRule.getOwemaxdays())){
			    	sRules.add(sectionRule);
			    }
			}
		}
		
		return sRules;
	}

	/**
	 * @throws OmcException 
	 * 根据客户等级,匹配相应规则
	* @Title: matchCreditLevel 
	* @Description: 信用等级匹配
	* @param @param curr
	* @param @param sectionRules
	* @param @return    设定文件 
	* @return List<SectionRule>    返回类型 
	* @throws
	 */
	private List<SectionRule> matchCreditLevel(Customer customer,List<SectionRule> sectionRules,String policyid) throws OmcException{
		ConfigContainer cfg = this.getConfig();
		//是否参照客户级别
		String  matchcreditlevel = cfg.getCfgPara(OmcCalKey.OMC_CFG_MATCH_CREDITLEVEL,this.getOmcobj().getTenantid(),policyid,"");
		List<SectionRule> midRules = new ArrayList<SectionRule>();
		
		if (MatchCreditLevel.MATCH.equals(matchcreditlevel)){
			if (StringUtils.isBlank(customer.getCustLevel())){
				throw new OmcException("matchCreditLevel", "客户信控级别为空" + customer);
			}
			
			for (Iterator<SectionRule> iterator = sectionRules.iterator(); iterator.hasNext();) {
				SectionRule sectionRule = iterator.next();
				if (StringUtils.isBlank(sectionRule.getCustlevel())){
					throw new OmcException("matchCreditLevel", "信控规则客户等级为空" + sectionRule.toString());
				}
				
				if (customer.getCustLevel().equals(sectionRule.getCustlevel())){
					midRules.add(sectionRule);
				}
			}
		}else{
			midRules.addAll(sectionRules);
		}
		return midRules;
	}	
	

	public RealTimeBalance getRealTimeBalance() {
		return realTimeBalance;
	}

	public void setRealTimeBalance(RealTimeBalance realTimeBalance) {
		this.realTimeBalance = realTimeBalance;
	}

	public InformationProcessor getInfo() {
		return info;
	}

	public void setInfo(InformationProcessor info) {
		this.info = info;
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
