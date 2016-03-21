package com.ai.baas.omc.topoligy.core.pojo;

/**
 * 账户信息
 */
public final class Account {

	private String  tenantId;
	private String	systemId;
	private String  accountId;
	private String	customerId;
	private String	acctType;
	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	@Override
	public String toString() {
		return "Account [tenantId=" + tenantId + ", systemId=" + systemId + ", accountId=" + accountId + ", customerId="
				+ customerId + ", acctType=" + acctType + "]";
	}
		
}
