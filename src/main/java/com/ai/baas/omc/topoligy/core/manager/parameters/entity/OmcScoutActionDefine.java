package com.ai.baas.omc.topoligy.core.manager.parameters.entity;

import java.io.Serializable;
/**
 * 指令定义
 * @author jackieliu
 *
 */
public final class OmcScoutActionDefine implements Serializable {
	
	private static final long serialVersionUID = 8471783599269137395L;
	private String	tenantId;
	private String	scoutType;
	private String	businessCode;
	private String	scoutRule;
	private String  infCommond;
	private String  smsTemplate;
	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getScoutType() {
		return scoutType;
	}
	public void setScoutType(String scoutType) {
		this.scoutType = scoutType;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getScoutRule() {
		return scoutRule;
	}
	public void setScoutRule(String scoutRule) {
		this.scoutRule = scoutRule;
	}
	public String getInfCommond() {
		return infCommond;
	}
	public void setInfCommond(String infCommond) {
		this.infCommond = infCommond;
	}
	public String getSmsTemplate() {
		return smsTemplate;
	}
	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}
	@Override
	public String toString() {
		return "OmcScoutActionDefine [tenantId=" + tenantId + ", scoutType=" + scoutType + ", businessCode="
				+ businessCode + ", scoutRule=" + scoutRule + ", infCommond=" + infCommond + ", smsTemplate="
				+ smsTemplate + "]";
	}
	
	
}
