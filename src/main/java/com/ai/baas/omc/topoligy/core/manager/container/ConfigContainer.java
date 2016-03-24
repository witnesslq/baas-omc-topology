package com.ai.baas.omc.topoligy.core.manager.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import org.apache.commons.lang.StringUtils;
import com.ai.baas.omc.topoligy.core.manager.parameters.dao.IConfigObtain;
import com.ai.baas.omc.topoligy.core.manager.parameters.dao.impl.ConfigObtain;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.OmcCalConf;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.OmcCalConfKey;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.OmcScoutActionDefine;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.Policy;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.PolicyConf;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.PolicyConfKey;
import com.ai.baas.omc.topoligy.core.manager.parameters.entity.PolicyKey;
import com.ai.baas.omc.topoligy.core.pojo.SectionRule;
import com.google.gson.JsonObject;

/**
 * 完成参数加载，匹配功能
 * @ClassName: ConfigContainer
 * @author lvsj
 * @date 2015年12月24日 下午3:16:20
 * 
 */
public final class ConfigContainer {
	// private static final Logger logger = LoggerFactory.getLogger(ConfigContainer.class);
	private IConfigObtain configObtainService;
	//信控策略集合
	private Map<PolicyKey, Policy> policyMap;
	//信控策略集合
	private Map<String, Policy> policyIdMap;
	//信控租户参数
	private Map<OmcCalConfKey, OmcCalConf> omcCalConfMap;
	//信控策略参数
	private Map<PolicyConfKey, PolicyConf> policyConfMap;
	//信控规则集合
	private Map<PolicyKey, List<SectionRule>> policyForRuleMap;
	//信控规则集合
	private Map<Integer, SectionRule> ruleMap;
	//指令集合
	private List<OmcScoutActionDefine> actionDefines;
	//系统配置信息
	private Map<String, String> sysconfig;

	public ConfigContainer() {
		configObtainService = new ConfigObtain();
	}

	public void configObtain() throws OmcException {
		List<Policy> policies = configObtainService.selectPolicyAll();
		List<SectionRule> sectionRules = configObtainService.selectSectionRuleAll();
		List<OmcScoutActionDefine> omcScoutActionDefines = configObtainService.selectActionAll();

		routePolicyAndRule(policies, sectionRules);
		routeOmcCalConf(configObtainService.selectOmcCfgAll());
		routePolicyConf(configObtainService.selectPolicyCfgAll());

		if (omcScoutActionDefines != null) {
			actionDefines = new ArrayList<OmcScoutActionDefine>();
			actionDefines.addAll(omcScoutActionDefines);
		}

	}

	/**
	 * 将策略和规则放到Map中
	 *
	 * @param list
	 * @param rules
	 * @throws OmcException
	 */
	private void routePolicyAndRule(List<Policy> list, List<SectionRule> rules) throws OmcException {
		policyMap = new HashMap<PolicyKey, Policy>();
		policyIdMap = new HashMap<String, Policy>();
		policyForRuleMap = new HashMap<PolicyKey, List<SectionRule>>();
		ruleMap = new HashMap<Integer, SectionRule>();

		if ((list == null) || (list.isEmpty())) {
			return;
		}
		//处理策略
		for (Policy policy:list) {
			PolicyKey policyKey = policy.getPolicyKey();

			if (policyMap.get(policyKey) == null) {
				policyMap.put(policyKey, policy);
			}

			if (policyIdMap.get(policy.getPolicyId()) == null) {
				policyIdMap.put(policy.getPolicyId(), policy);
			}
		}
		//处理规则
		Iterator<SectionRule> iterator = rules.iterator();
		while (iterator.hasNext()) {
			SectionRule sectionRule = iterator.next();
			if (ruleMap.get(sectionRule.getScoutruleid()) == null) {
				ruleMap.put(sectionRule.getScoutruleid(), sectionRule);
			}

			Policy policy = policyIdMap.get(sectionRule.getPolicyId());
			if (policy == null) {
				throw new OmcException("routePolicyAndRule", "信控策略和规则配置不一致");
			}

			PolicyKey policyKey = policy.getPolicyKey();
			if (!sectionRule.getTenantid().equals(policyKey.getTenantid())) {
				continue;
			}
			List<SectionRule> sectionRules = policyForRuleMap.get(policyKey);
			if (sectionRules == null) {
				sectionRules = new ArrayList<SectionRule>();
				sectionRules.add(sectionRule);
				policyForRuleMap.put(policyKey, sectionRules);
			} else {
				boolean bFind = false;
				for (SectionRule st:sectionRules) {
					if (st.getScoutruleid() == sectionRule.getScoutruleid()) {
						bFind = true;
					}
				}
				if (!bFind) {
					sectionRules.add(sectionRule);
				}
			}
		}
	}

	/**
	 * 将信控租户参数放到Map中
	 * @param list
	 */
	private void routeOmcCalConf(List<OmcCalConf> list) {
		omcCalConfMap = new HashMap<OmcCalConfKey, OmcCalConf>();
		if ((list == null) || (list.isEmpty())) {
			return;
		}

		for (OmcCalConf omcCalConf:list) {
			OmcCalConfKey omcCalConfKey = new OmcCalConfKey();
			omcCalConfKey.setTenantid(omcCalConf.getConfkey().getTenantid());
			omcCalConfKey.setConfkey(omcCalConf.getConfkey().getConfkey());

			if (omcCalConfMap.get(omcCalConfKey) == null) {
				omcCalConfMap.put(omcCalConfKey, omcCalConf);
			}
		}

	}

	/**
	 * 将策略参数放到Map中
	 *
	 * @param list
	 * @return void
	 */
	private void routePolicyConf(List<PolicyConf> list) {
		policyConfMap = new HashMap<PolicyConfKey, PolicyConf>();

		if ((list == null) || (list.isEmpty())) {
			return;
		}

		for (PolicyConf policyConf: list) {
			PolicyConfKey policyConfKey = policyConf.getPolicyConfKey();

			if (policyConfMap.get(policyConfKey) == null) {
				policyConfMap.put(policyConfKey, policyConf);
			}

		}
	}

	/**
	 * 获取指定虚商标识和策略类型的策略
	 * @param tenantId
	 * @param businessCode
	 * @return
	 */
	public Policy getPolicy(String tenantId, String businessCode) {
		PolicyKey policyKey = new PolicyKey();
		policyKey.setTenantid(tenantId);
		policyKey.setPolicytype(businessCode);
		return policyMap.get(policyKey);
	}

	public Policy getPolicyById(String policyId) {
		return policyIdMap.get(policyId);
	}
	/**
	 * 获取指定信控策略的信控规则
	 * @param policyKey
	 * @return
	 */
	public List<SectionRule> getSectionRules(PolicyKey policyKey) {
		return policyForRuleMap.get(policyKey);
	}

	public SectionRule getSectionRule(Integer ruleid) {
		return ruleMap.get(ruleid);
	}

	/**
	 * 获取指令定义
	 * @param tenantid
	 * @param businessCode
	 * @param ruleId
	 * @param scoutType
	 * @return
     * @throws OmcException
     */
	public OmcScoutActionDefine getActionDefine(String tenantid, String businessCode, String ruleId, String scoutType)
			throws OmcException {
		OmcScoutActionDefine omcScoutAction = null;
		if ((actionDefines != null) && (!actionDefines.isEmpty())) {
			for (OmcScoutActionDefine omcScoutActionDefine: actionDefines) {
				if (!omcScoutActionDefine.getTenantId().equals(tenantid)
						|| !omcScoutActionDefine.getScoutType().equals(scoutType)
						|| !omcScoutActionDefine.getBusinessCode().equals(businessCode)) {
					continue;
				}

				if (StringUtils.isNotBlank(ruleId)
						&& !"-1".equals(ruleId) && !"0".equals(ruleId)
						&& !omcScoutActionDefine.getScoutRule().equals(ruleId)) {
					continue;
				}
				omcScoutAction = omcScoutActionDefine;
				break;
			}
		}
		//如果未找到相关指令,则抛出异常
		if (omcScoutAction == null) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("tenantid", tenantid);
			jsonObject.addProperty("businesscode", businessCode);
			jsonObject.addProperty("scoruletype", scoutType);
			jsonObject.addProperty("ruleId", ruleId);
			throw new OmcException("start", "没取到对应的指令配置" + jsonObject.toString());
		}

		return omcScoutAction;
	}

	/**
	 * 获取租户/策略参数
	 * @param parakey
	 * @param tenantid
	 * @param policyid
	 * @param ruleid
	 * @return
     * @throws OmcException
     */
	public String getCfgPara(String parakey, String tenantid, String policyid, String ruleid) throws OmcException {
		if (StringUtils.isBlank(parakey)) {
			throw new OmcException("getActionDefine", "需要指定参数关键字");
		}
		String para_value = "";
		if (StringUtils.isNotBlank(tenantid)) {
			OmcCalConfKey omcCalConfKey = new OmcCalConfKey();
			omcCalConfKey.setTenantid(tenantid);
			omcCalConfKey.setConfkey(parakey);
			//信控租户
			OmcCalConf omcCalConf = omcCalConfMap.get(omcCalConfKey);
			if (omcCalConf != null) {
				para_value = omcCalConf.getConfvalue();
			}
		}

		if (StringUtils.isNotBlank(tenantid)
				&& StringUtils.isNotBlank(policyid)) {
			PolicyConfKey policyConfKey = new PolicyConfKey();
			policyConfKey.setTenantid(tenantid);
			policyConfKey.setConfkey(parakey);
			policyConfKey.setPolicyid(policyid);
			//信控策略
			PolicyConf policyConf = policyConfMap.get(policyConfKey);
			if (policyConf != null) {
				para_value = policyConf.getConfvalue();
			}
		}
		if (StringUtils.isBlank(para_value)) {
			para_value = getDefaultParaCfg(parakey);
		}
		if (StringUtils.isBlank(para_value)) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("parakey", parakey);
			jsonObject.addProperty("tenantid", tenantid);
			jsonObject.addProperty("policyid", policyid);
			jsonObject.addProperty("ruleid", ruleid);
			throw new OmcException("getCfgPara", "参数值为空，请进行配置或设置缺省值" + jsonObject.toString());
		}
		return para_value;
	}

	public String getDefaultParaCfg(String parakey) {
		return null;
	}

	public Map<String, String> getSysconfig() {
		return sysconfig;
	}

	public void setSysconfig(Map<String, String> sysconfig) {
		this.sysconfig = sysconfig;
	}
}
