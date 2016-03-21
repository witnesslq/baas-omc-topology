package com.ai.baas.omc.topoligy.core.pojo;

import java.sql.Timestamp;

public class OmcScoutLog {
    private Long logid;

    private String sourcetype;

    private String ownertype;

    private String ownerId;

    private String businessCode;

    private String scoutType;

    private String status;

    private Timestamp insettime;

    private String scoutRule;

    private String balanceinfo;

    private String parainfo;

    private String tenantId;

    private String systemId;

    public Long getLogid() {
        return logid;
    }

    public void setLogid(Long logid) {
        this.logid = logid;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype == null ? null : sourcetype.trim();
    }

    public String getOwnertype() {
        return ownertype;
    }

    public void setOwnertype(String ownertype) {
        this.ownertype = ownertype == null ? null : ownertype.trim();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId == null ? null : ownerId.trim();
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode == null ? null : businessCode.trim();
    }

    public String getScoutType() {
        return scoutType;
    }

    public void setScoutType(String scoutType) {
        this.scoutType = scoutType == null ? null : scoutType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Timestamp getInsettime() {
		if(insettime != null){
			return (Timestamp) insettime.clone();
		}else{
			return null;			
		}
    }

    public void setInsettime(Timestamp insettime) {
		if(insettime != null){
			this.insettime = (Timestamp) insettime.clone();
		}else{
			this.insettime = null;
		}
    }

    public String getScoutRule() {
        return scoutRule;
    }

    public void setScoutRule(String scoutRule) {
        this.scoutRule = scoutRule == null ? null : scoutRule.trim();
    }

    public String getBalanceinfo() {
        return balanceinfo;
    }

    public void setBalanceinfo(String balanceinfo) {
        this.balanceinfo = balanceinfo == null ? null : balanceinfo.trim();
    }

    public String getParainfo() {
        return parainfo;
    }

    public void setParainfo(String parainfo) {
        this.parainfo = parainfo == null ? null : parainfo.trim();
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
}