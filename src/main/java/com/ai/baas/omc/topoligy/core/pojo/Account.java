package com.ai.baas.omc.topoligy.core.pojo;

/**
 * 账户信息
 */
public final class Account {

	private String  tenantId;
	private String  accountId;
	//属主类型 CUST：某个客户的账户,USER：某个用户的账户
	private String	ownerType;
	//属主ID CUST_ID或者USER_ID,与属主类型匹配
	private String	ownerId;
	private String	acctType;
	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return "Account [tenantId=" + tenantId + ", accountId=" + accountId
				+ ", ownerType=" + ownerType + ", ownerId=" + ownerId + ", acctType=" + acctType + "]";
	}
		
}
