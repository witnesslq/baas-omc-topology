package com.ai.baas.omc.topoligy.core.pojo;

import java.sql.Timestamp;

public class OmcUrgeStatus {
	
    private Long urgeSerial;

    private String ownerType;

    private String ownerId;

    private String urgeType;

    private String businessCode;

    private String status;

    private String lastStatus;

    private Timestamp statusTime;

    private Timestamp notifyTime;

    private Integer notifyTimes;

    private String notifyStatus;

    private String notifyType;

    private String scoutInfo;

    private String systemId;

    private String tenantId;

    public Long getUrgeSerial() {
        return urgeSerial;
    }

    public void setUrgeSerial(Long urgeSerial) {
        this.urgeSerial = urgeSerial;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType == null ? null : ownerType.trim();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId == null ? null : ownerId.trim();
    }

    public String getUrgeType() {
        return urgeType;
    }

    public void setUrgeType(String urgeType) {
        this.urgeType = urgeType == null ? null : urgeType.trim();
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode == null ? null : businessCode.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus == null ? null : lastStatus.trim();
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
        this.notifyStatus = notifyStatus == null ? null : notifyStatus.trim();
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType == null ? null : notifyType.trim();
    }

    public String getScoutInfo() {
        return scoutInfo;
    }

    public void setScoutInfo(String scoutInfo) {
        this.scoutInfo = scoutInfo == null ? null : scoutInfo.trim();
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId == null ? null : systemId.trim();
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

	@Override
	public String toString() {
		return "OmcUrgeStatus [urgeSerial=" + urgeSerial + ", ownerType=" + ownerType + ", ownerId=" + ownerId
				+ ", urgeType=" + urgeType + ", businessCode=" + businessCode + ", status=" + status + ", lastStatus="
				+ lastStatus + ", statusTime=" + statusTime + ", notifyTime=" + notifyTime + ", notifyTimes="
				+ notifyTimes + ", notifyStatus=" + notifyStatus + ", notifyType=" + notifyType + ", scoutInfo="
				+ scoutInfo + ", systemId=" + systemId + ", tenantId=" + tenantId + "]";
	}
    
}