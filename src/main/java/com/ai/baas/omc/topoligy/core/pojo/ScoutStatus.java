package com.ai.baas.omc.topoligy.core.pojo;

import java.sql.Timestamp;

public final class ScoutStatus {
	private Long scoSeq;
	private String  tenantId;
	private String	systemId;
	private String subsId;
	private String acctId;
	private String custId;
	private String businessCode;
	private String status;
	private String lastStatus;
	private Timestamp statusTime;
	private Timestamp notifyTime;
	private Integer notifyTimes;
	private String notifyStatus;
	private String notifyType;
	private String scoutInfo;
	
	
	public Long getScoSeq() {
		return scoSeq;
	}
	public void setScoSeq(Long scoSeq) {
		this.scoSeq = scoSeq;
	}
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
	public String getSubsId() {
		return subsId;
	}
	public void setSubsId(String subsId) {
		this.subsId = subsId;
	}
	public String getAcctId() {
		return acctId;
	}
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastStatus() {
		return lastStatus;
	}
	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}
	public Timestamp getStatusTime() {
		if(statusTime != null){
			return (Timestamp) statusTime.clone();
		}else{
			return null;			
		}
	}
	public void setStatusTime(Timestamp statusTime) {
		if(statusTime != null){
			this.statusTime = (Timestamp) statusTime.clone();
		}else{
			this.statusTime = null;
		}
	}
	public Timestamp getNotifyTime() {
		if(notifyTime != null){
			return (Timestamp) notifyTime.clone();
		}else{
			return null;			
		}
	}
	public void setNotifyTime(Timestamp notifyTime) {
		if(notifyTime != null){
			this.notifyTime = (Timestamp) notifyTime.clone();
		}else{
			this.notifyTime = null;
		}
	}
	public Integer getNotifyTimes() {
		return notifyTimes;
	}
	public void setNotifyTimes(Integer notifyTimes) {
		this.notifyTimes = notifyTimes;
	}
	public String getNotifyStatus() {
		return notifyStatus;
	}
	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public String getScoutInfo() {
		return scoutInfo;
	}
	public void setScoutInfo(String scoutInfo) {
		this.scoutInfo = scoutInfo;
	}
	@Override
	public String toString() {
		return "ScoutStatus [tenantId=" + tenantId + ", systemId=" + systemId + ", subsId=" + subsId + ", acctId="
				+ acctId + ", custId=" + custId + ", businessCode=" + businessCode + ", status=" + status
				+ ", lastStatus=" + lastStatus + ", statusTime=" + statusTime + ", notifyTime=" + notifyTime
				+ ", notifyTimes=" + notifyTimes + ", notifyStatus=" + notifyStatus + ", notifyType=" + notifyType
				+ ", scoutInfo=" + scoutInfo + "]";
	}



}
