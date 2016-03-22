package com.ai.baas.omc.topoligy.core.pojo;

/**
 * 客户信息
 */
public final class Customer {
	private String	customerId;
	private String	custLevel;
	private String	custType;
	private String  tenantId;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustLevel() {
		return custLevel;
	}
	public void setCustLevel(String custLevel) {
		this.custLevel = custLevel;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", custLevel=" + custLevel + ", custType=" + custType
				+ ", tenantId=" + tenantId + "]";
	}
}
