package com.ai.baas.omc.topoligy.core.business;

import java.util.List;

import com.ai.baas.omc.topoligy.core.business.base.BaseProcess;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.Policy;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.SectionRule;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.baas.omc.topoligy.core.constant.OwnerType;
import com.ai.baas.omc.topoligy.core.manager.service.SubsUserService;
import com.ai.baas.omc.topoligy.core.manager.service.shm.SubsUserServiceImplShm;
import com.ai.baas.omc.topoligy.core.pojo.Account;
import com.ai.baas.omc.topoligy.core.pojo.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class EventProcessor extends BaseProcess {
		
	public  static final String DF_POLICY =  OmcCalKey.OMC_POLICY_ID;
	public  static final String DF_RULES =  OmcCalKey.OMC_RULE_ID_LIST;
	public  static final String DF_RULE_ID =  OmcCalKey.OMC_RULE_ID;
	private static final String OWERTYPE = null;
	private  SubsUserService subsUserService; 
	
	public EventProcessor(ConfigContainer cfg, OmcObj obj, JsonObject data) throws OmcException {
		super(cfg, obj, data);
	}

	@Override
	public void process() throws OmcException {
		OmcObj obj = getOmcobj();
		ConfigContainer configContainer = getConfig();
		//获取信控策略  update by 2016-05-17
		//变更点 由根据租户+event_type找的策略id  现根据用户先查用户策略，如果没查到再查对应的公共策略
		subsUserService = new SubsUserServiceImplShm();
		User usrs = null;
		if (OwnerType.SERV.equals(obj.getOwertype())){
			usrs = subsUserService.selectById(obj.getTenantid(),obj.getOwerid());
			if (null == usrs){
				throw new OmcException("Information","没有取到用户信息infoOwner" + obj.toString());
			}
		}
		Policy policy = null;
		if (!StringUtil.isBlank(usrs.getPolicy_id())) {
			policy = configContainer.getPolicyById(usrs.getPolicy_id());
		}
		if (null == policy) {
			System.out.println("用户["+obj.toString()+"]没有信控方案，采用默认的信控方案");
			policy = configContainer.getPolicy(obj.getTenantid(),obj.getBusinesscode());
			if (null == policy) {
				throw new OmcException("EventProcessor", "获取信控策略失败,请检查配置" + obj.toString());
			}
		}
//		//获取信控策略
//		Policy policy = configContainer.getPolicy(obj.getTenantid(),obj.getBusinesscode());
//
//		if (null == policy) {
//			throw new OmcException("EventProcessor", "获取信控策略失败,请检查配置" + obj.toString());
//		}
		// 查询策略所对应的规则
		List<SectionRule> sectionRules = configContainer.getSectionRules(policy.getPolicyKey());
		// 不存在对应的信控规则
		if (null == sectionRules ||sectionRules.isEmpty()){
			throw new OmcException("EventProcess", "获取policy[" + policy.getPolicyKey().toString() + "]对应的信控失败，请检查配置");
		}
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(OmcCalKey.OMC_POLICY_ID, policy.getPolicyId());
		JsonArray jsonArray = new  JsonArray();
		for (SectionRule sectionRule:sectionRules) {
			JsonObject jsonobj = new JsonObject();
			jsonobj.addProperty(OmcCalKey.OMC_RULE_ID, sectionRule.getScoutruleid());
			jsonArray.add(jsonobj);
		}
		jsonObject.add(OmcCalKey.OMC_RULE_ID_LIST, jsonArray);
		setOutput(jsonObject);
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
