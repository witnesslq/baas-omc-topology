package com.ai.baas.omc.topoligy.core.manager.parameters.dao.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;
import com.ai.baas.omc.topoligy.core.manager.parameters.dao.IConfigObtain;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.OmcCalConf;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.OmcCalConfKey;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.OmcScoutActionDefine;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.Policy;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.PolicyConf;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.PolicyConfKey;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.PolicyKey;
import com.ai.baas.omc.topoligy.core.pojo.SectionRule;
import com.ai.baas.omc.topoligy.core.util.DateUtils;

public final class ConfigObtain implements IConfigObtain {
	
	private  static final Logger logger = LoggerFactory.getLogger(ConfigObtain.class);	
	
	private static final JdbcProxy dbproxy = JdbcProxy.getInstance();
	
	/**
	 * 获取信控所有租户参数
	 */
	@Override
	public List<OmcCalConf> selectOmcCfgAll() throws OmcException {

		try {
			StringBuilder sql = new StringBuilder("");
//			String tableName ="omc_tenant_para";
			sql.append("select tenant_id, para_type, para_value, para_name from ");
			//设置表名
			sql.append("omc_tenant_para").append(" where ");
			sql.append(" 1 =").append("1");
			Connection connection = dbproxy.getConnection();
   		    List<Map<String,Object>> rows =  JdbcTemplate.query(sql.toString(), connection, new MapListHandler());
   		 
   		    if ((rows == null)||(rows.isEmpty())){
				logger.info("信控计算参数无配置数据【omc_tenant_para】");
				return Collections.emptyList();
   		    }
   		    
   		    List<OmcCalConf> omcCalConfs = new ArrayList<OmcCalConf>();
	    	for (Map<String, Object> map : rows) {
	    		OmcCalConf omcCalConf = new OmcCalConf();
	    		OmcCalConfKey omcCalConfKey = new OmcCalConfKey();
	    		omcCalConfKey.setTenantid(map.get("tenant_id").toString());
	    		omcCalConfKey.setConfkey(map.get("para_type").toString());
	    		omcCalConf.setConfkey(omcCalConfKey);
	    		omcCalConf.setConfvalue(map.get("para_value").toString());
	    		
	    		omcCalConfs.add(omcCalConf);

			}
			logger.info("信控计算装载参数【omc_tenant_para】共【"+omcCalConfs.size()+"】条");
	    	return omcCalConfs;

		} catch (Exception e) {
			throw new OmcException("ConfigObtain",e);
		}
	}

	/**
	 * 获取信控所有策略参数
	 */
	@Override
	public List<PolicyConf> selectPolicyCfgAll() throws OmcException{
		try {
			StringBuilder sql = new StringBuilder("");
//			String tableName ="omc_policy_para";
			sql.append("select tenant_id,policyId, para_type, para_value, para_name from ");
			sql.append("omc_policy_para").append(" where ");
			sql.append(" 1 =").append("1");

			Connection connection = dbproxy.getConnection();
   		    List<Map<String,Object>> rows =  JdbcTemplate.query(sql.toString(), connection, new MapListHandler());
			
   		    if ((rows == null)||(rows.isEmpty())){
				logger.info("信控计算参数无配置数据【omc_policy_para】");
				return Collections.emptyList();
   		    }
   		    
   		    List<PolicyConf> policyConfs = new ArrayList<PolicyConf>();

	    	for (Map<String, Object> map : rows) {
	    		PolicyConf  policyConf = new PolicyConf();
	    		PolicyConfKey policyConfKey = new PolicyConfKey();
	    		policyConfKey.setTenantid(map.get("tenant_id").toString());
	    		policyConfKey.setConfkey(map.get("para_type").toString());
	    		policyConfKey.setPolicyid(map.get("policyId").toString());
	    		policyConf.setPolicyConfKey(policyConfKey);
	    		policyConf.setConfvalue(map.get("para_value").toString());

	    		policyConfs.add(policyConf);

			}
			logger.info("信控计算装载参数【omc_policy_para】共【"+policyConfs.size()+"】条");
	    	return policyConfs;

		} catch (Exception e) {
			throw new OmcException("ConfigObtain",e);
		}
	}

	/**
	 * 获取信控所有策略
	 */
	@Override
	public List<Policy> selectPolicyAll() throws OmcException {
		try {
			StringBuilder sql = new StringBuilder("");
//			String tableName ="omc_scout_policy";
			sql.append("select policyId,policy_name, tenant_id,policyType, status, eff_date,exp_date from ");
			sql.append("omc_scout_policy").append(" where ");
			sql.append(" 1 =").append("1");
			Connection connection = dbproxy.getConnection();
   		    List<Map<String,Object>> rows =  JdbcTemplate.query(sql.toString(), connection, new MapListHandler());

   		    if ((rows == null)||(rows.isEmpty())){
   		    	throw new OmcException("selectPolicyAll", "信控计算策略无配置数据【omc_scout_policy】");
   		    }
   		    
   		    List<Policy> policies = new ArrayList<Policy>();

	    	for (Map<String, Object> map : rows) {
	    		
	    		Policy  policie = new Policy();
	    		PolicyKey policyKey = new PolicyKey();
	    		policyKey.setPolicytype(map.get("policyType").toString());
	    		policyKey.setTenantid(map.get("tenant_id").toString());
	    		policie.setPolicyKey(policyKey);
	    		policie.setPolicyId(map.get("policyId").toString());
	    		policie.setStatus(map.get("status").toString());
	    		policie.setEffdate(DateUtils.getTimestamp(map.get("eff_date").toString(), "yyyy-MM-dd HH:mm:ss"));
	    		policie.setExpdate(DateUtils.getTimestamp(map.get("exp_date").toString(), "yyyy-MM-dd HH:mm:ss"));
	    		policie.setPolicyDescribe(map.get("policy_name").toString());

	    		policies.add(policie);

			}
			logger.info("信控计算装载参数【omc_scout_policy】共【"+policies.size()+"】条");
	    	return policies;

		} catch (Exception e) {
			throw new OmcException("ConfigObtain",e);
		}
	}

	/**
	 * 获取信控所有规则
	 */
	@Override
	public List<SectionRule> selectSectionRuleAll() throws OmcException {
		
		try {
			StringBuilder sql = new StringBuilder("");
//			String tableName ="omc_scout_rule";
			sql.append("select rule_id,policyId,scout_rule,scout_type,balance_floor,balance_ceil,owe_maxdays,owe_mindays,");
			sql.append("charge_ceil,charge_floor,cust_type,acct_type,user_type,cust_level,tenant_id,section_type from ");
			sql.append("omc_scout_rule").append(" where ");
			sql.append(" 1 =").append("1");

			Connection connection = dbproxy.getConnection();
   		    List<Map<String,Object>> rows =  JdbcTemplate.query(sql.toString(), connection, new MapListHandler());

   		    if ((rows == null)||(rows.isEmpty())){
   		    	throw new OmcException("selectPolicyAll", "信控计算策略无配置数据【selectSectionRuleAll】");
   		    }
   		    
   		    List<SectionRule> sectionRules = new ArrayList<SectionRule>();

	    	for (Map<String, Object> map : rows) {
	    		
	    		SectionRule sectionRule = new  SectionRule();
	    		sectionRule.setAccttype(map.get("acct_type").toString());
	    		sectionRule.setBalanceceil(Long.parseLong(map.get("balance_ceil").toString()));
	    		sectionRule.setBalancefloor(Long.parseLong(map.get("balance_floor").toString()));
	    		sectionRule.setChargeceil(Long.parseLong(map.get("charge_ceil").toString()));
	    		sectionRule.setChargefloor(Long.parseLong(map.get("charge_floor").toString()));
	    		sectionRule.setCustlevel(map.get("cust_level").toString());
	    		sectionRule.setCusttype(map.get("cust_type").toString());
	    		sectionRule.setOwemaxdays(Integer.parseInt(map.get("owe_maxdays").toString()));
	    		sectionRule.setOwemindays(Integer.parseInt(map.get("owe_mindays").toString()));
	    		sectionRule.setPolicyId(map.get("policyId").toString());
	    		sectionRule.setScoutruleid(Integer.parseInt(map.get("rule_id").toString()));
	    		sectionRule.setScouttype(map.get("scout_type").toString());
	    		sectionRule.setTenantid(map.get("tenant_id").toString());
	    		sectionRule.setUsertype(map.get("user_type").toString());
	    		sectionRule.setSectiontype(map.get("section_type").toString());
	    		
	    		sectionRules.add(sectionRule);

			}
			logger.info("信控计算装载参数【omc_scout_rule】共【"+sectionRules.size()+"】条");
	    	return sectionRules;

		} catch (Exception e) {
			throw new OmcException("ConfigObtain",e);
		}
	}

	/**
	 * 获取信控所有指令定义
	 */
	@Override
	public List<OmcScoutActionDefine> selectActionAll() throws OmcException {
		try {
			StringBuilder sql = new StringBuilder("");
//			String tableName ="omc_scout_action_define";
			sql.append("select tenant_id,scout_type,business_code,scout_rule,inf_commond,sms_template from ");
			sql.append("omc_scout_action_define").append(" where ");
			sql.append(" 1 =").append("1");

			Connection connection = dbproxy.getConnection();
   		    List<Map<String,Object>> rows =  JdbcTemplate.query(sql.toString(), connection, new MapListHandler());

   		    if ((rows == null)||(rows.isEmpty())){
   		    	throw new OmcException("selectActionAll", "信控指令未定义【omc_scout_action_define】"+sql.toString());
   		    }
   		    
   		    List<OmcScoutActionDefine> omcScoutActionDefines = new ArrayList<OmcScoutActionDefine>();

	    	for (Map<String, Object> map : rows) {
	    		
	    		OmcScoutActionDefine omcScoutActionDefine = new  OmcScoutActionDefine();

	    		omcScoutActionDefine.setBusinessCode(map.get("business_code").toString());
	    		omcScoutActionDefine.setInfCommond(map.get("inf_commond").toString());
	    		omcScoutActionDefine.setScoutRule(map.get("scout_rule").toString());
	    		omcScoutActionDefine.setScoutType(map.get("scout_type").toString());
	    		omcScoutActionDefine.setSmsTemplate(map.get("sms_template").toString());
	    		omcScoutActionDefine.setTenantId(map.get("tenant_id").toString());

	    		omcScoutActionDefines.add(omcScoutActionDefine);

			}
			logger.info("信控计算装载参数【omc_scout_action_define】共【"+omcScoutActionDefines.size()+"】条");
	    	return omcScoutActionDefines;

		} catch (Exception e) {
			throw new OmcException("ConfigObtain",e);
		}
	}

}
