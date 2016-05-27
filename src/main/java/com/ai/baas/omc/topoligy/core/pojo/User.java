package com.ai.baas.omc.topoligy.core.pojo;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * 用户信息
 */
public final class User{
	private String	subsid;
	private String	accountid;
	private String	customerid;
	private String	serviceId;
	private String	substype;
	private String	subsstatus;
	private String	servicestatus;
	private String	statuschgtype;
	private String	provincecode;
	private String	citycode;
	private String  tenantid;
	private String	systemid;
	private String	servicetype;
	private Timestamp activetime;
	private Timestamp inactivetime;
	private String factorcode;
	//因变更获取信控策略而增加  update by 2016-05-17
	private String policy_id;
	
	public String getSubsid() {
		return subsid;
	}
	public void setSubsid(String subsid) {
		this.subsid = subsid;
	}
	public String getAccountid() {
		return accountid;
	}
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getSubstype() {
		return substype;
	}
	public void setSubstype(String substype) {
		this.substype = substype;
	}
	public String getSubsstatus() {
		return subsstatus;
	}
	public void setSubsstatus(String subsstatus) {
		this.subsstatus = subsstatus;
	}
	public String getServicestatus() {
		return servicestatus;
	}
	public void setServicestatus(String servicestatus) {
		this.servicestatus = servicestatus;
	}
	public String getStatuschgtype() {
		return statuschgtype;
	}
	public void setStatuschgtype(String statuschgtype) {
		this.statuschgtype = statuschgtype;
	}

	public String getProvincecode() {
		return provincecode;
	}
	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getServicetype() {
		return StringUtils.isBlank(servicetype)?"0":servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	public Timestamp getActivetime() {
		if(activetime != null){
			return (Timestamp) activetime.clone();
		}else{
			return null;			
		}
	}
	public void setActivetime(Timestamp activetime) {
		
		if(activetime != null){
			this.activetime = (Timestamp) activetime.clone();
		}else{
			this.activetime = null;
		}
		
	}
	public Timestamp getInactivetime() {
		if(inactivetime != null){
			return (Timestamp) inactivetime.clone();
		}else{
			return null;			
		}
	}
	public void setInactivetime(Timestamp inactivetime) {

		if(inactivetime != null){
			this.inactivetime = (Timestamp) inactivetime.clone();
		}else{
			this.inactivetime = null;
		}
	}
	public String getFactorcode() {
		return factorcode;
	}
	public void setFactorcode(String factorcode) {
		this.factorcode = factorcode;
	}
	public String getPolicy_id() {
		return policy_id;
	}
	public void setPolicy_id(String policy_id) {
		this.policy_id = policy_id;
	}
	
	@Override
	public String toString() {
		return "User [subsid=" + subsid + ", accountid=" + accountid + ", customerid=" + customerid + ", serviceId="
				+ serviceId + ", substype=" + substype + ", subsstatus=" + subsstatus + ", servicestatus="
				+ servicestatus + ", statuschgtype=" + statuschgtype + ", provincecode="
				+ provincecode + ", citycode=" + citycode + ", tenantid=" + tenantid + ", systemid=" + systemid
				+ ", servicetype=" + servicetype + ", activetime=" + activetime + ", inactivetime=" + inactivetime
				+ ", factorcode=" + factorcode + ", policy_id=" + policy_id + "]";
	}




}
