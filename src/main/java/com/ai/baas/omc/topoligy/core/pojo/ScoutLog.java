package com.ai.baas.omc.topoligy.core.pojo;

import java.util.List;

public final class ScoutLog {
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	
	private long logid;
	private String eventId;
	private OmcObj owner;
	private String policyId;
	private String sourceType;
	private String scostatus;
	private double creditValue;
	private SectionRule sectionRules;
	private Customer customer;
	private List<User> users;

	private List<Account> accounts;
    private RealTimeBalance realTimeBalance;
    
	public long getLogid() {
		return logid;
	}

	public void setLogid(long logid) {
		this.logid = logid;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getScostatus() {
		return scostatus;
	}

	public void setScostatus(String scostatus) {
		this.scostatus = scostatus;
	}

	public double getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(double creditValue) {
		this.creditValue = creditValue;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public RealTimeBalance getRealTimeBalance() {
		return realTimeBalance;
	}

	public void setRealTimeBalance(RealTimeBalance realTimeBalance) {
		this.realTimeBalance = realTimeBalance;
	}

	public OmcObj getOwner() {
		return owner;
	}

	public void setOwner(OmcObj owner) {
		this.owner = owner;
	}
	

	public SectionRule getSectionRules() {
		return sectionRules;
	}

	public void setSectionRules(SectionRule sectionRules) {
		this.sectionRules = sectionRules;
	}

	@Override
	public String toString() {
		return "ScoutLog [logid=" + logid + ", eventId=" + eventId 
				+ ", owner=" + owner + ", policyId=" + policyId + ", sourceType=" + sourceType + ", scostatus="
				+ scostatus + ", creditValue=" + creditValue + ", sectionRules=" + sectionRules + ", customer="
				+ customer + ", users=" + users + ", accounts=" + accounts + ", realTimeBalance=" + realTimeBalance
				+  "]";
	}

    

}
