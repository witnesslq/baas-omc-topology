package com.ai.baas.omc.topoligy.core.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public final class OmcBmsInterface implements Serializable {
	
	private static final long serialVersionUID = 7736872288058480859L;

	private Long serialNo;

    private String acctid;

    private String custid;

    private String subsid;
	    
    private String bmsData;

    private String interfaceData;

    private String scoutType;

    private Timestamp insertTime;

    private Integer dealFlag;

    private Timestamp dealTime;

    private String remark;

    private Integer retryTimes;

    private String tenantId;

    private String systemId;

    public Long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Long serialNo) {
        this.serialNo = serialNo;
    }

    public String getBmsData() {
        return bmsData;
    }

    public void setBmsData(String bmsData) {
        this.bmsData = bmsData == null ? null : bmsData.trim();
    }

    public String getInterfaceData() {
        return interfaceData;
    }

    public void setInterfaceData(String interfaceData) {
        this.interfaceData = interfaceData == null ? null : interfaceData.trim();
    }

    public String getAcctId() {
        return acctid;
    }

    public void setAcctId(String acctid) {
        this.acctid = acctid == null ? null : acctid.trim();
    }

    public String getSubsId() {
        return subsid;
    }

    public void setSubsId(String subsid) {
        this.subsid = subsid == null ? null : subsid.trim();
    }
    
    public String getCustId() {
        return custid;
    }

    public void setCustId(String custid) {
        this.custid = custid == null ? null : custid.trim();
    }
    
    public String getScoutType() {
        return scoutType;
    }

    public void setScoutType(String scoutType) {
        this.scoutType = scoutType == null ? null : scoutType.trim();
    }

    public Timestamp getInsertTime() {
		if(insertTime != null){
			return (Timestamp) insertTime.clone();
		}else{
			return null;			
		}
    }

    public void setInsertTime(Timestamp insertTime) {
		if(insertTime != null){
			this.insertTime = (Timestamp) insertTime.clone();
		}else{
			this.insertTime = null;
		}
    }

    public Integer getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(Integer dealFlag) {
        this.dealFlag = dealFlag;
    }

    public Timestamp getDealTime() {
		if(dealTime != null){
			return (Timestamp) dealTime.clone();
		}else{
			return null;			
		}
    }

    public void setDealTime(Timestamp dealTime) {
		if(dealTime != null){
			this.dealTime = (Timestamp) dealTime.clone();
		}else{
			this.dealTime = null;
		}
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId == null ? null : systemId.trim();
    }

	@Override
	public String toString() {
		return "OmcBmsInterface [serialNo=" + serialNo  + ", bmsData=" + bmsData + ", interfaceData=" + interfaceData + ", accountId="
				+ acctid + ", subsId=" + subsid + ", scoutType="
				+ scoutType 
				+ ", insertTime=" + insertTime + ", dealFlag=" + dealFlag + ", dealTime=" + dealTime + ", remark="
				+ remark  + ", retryTimes=" + retryTimes + ", tenantId="
				+ tenantId + ", systemId=" + systemId + "]";
	}
    
    
}