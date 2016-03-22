package com.ai.baas.omc.topoligy.core.util;

import java.util.ArrayList;
import java.util.List;

import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.SectionRule;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public final class OmcUtils {
	private OmcUtils(){
		
	}

	/**
	 * 根据规则id获取规则实例
	 * @param cfgContainer
	 * @param data
     * @return
     */
	public static final List<SectionRule> toSectionRules(ConfigContainer cfgContainer, String data){
		Gson gson = new Gson();
		JsonArray Rules = gson.fromJson(data, JsonArray.class);
		List<SectionRule> sectionRules = new ArrayList<SectionRule>();
		for (JsonElement jsonElement : Rules) {
			String rule = jsonElement.getAsJsonObject().get(OmcCalKey.OMC_RULE_ID).getAsString();
			sectionRules.add(cfgContainer.getSectionRule(Integer.parseInt(rule)));
		}
		return sectionRules;

	}
}
