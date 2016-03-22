package com.ai.baas.omc.topoligy.core.pojo;

import java.util.Date;

/**
 * 免催停信息
 */
public final class SpeUrgeStop  {
	private String tenantid;
	private String ownertype;
	private String ownerid;
	private String speType;
	private Date effDate;
	private Date expDate;
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getOwnertype() {
		return ownertype;
	}
	public void setOwnertype(String ownertype) {
		this.ownertype = ownertype;
	}
	public String getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}
	public String getSpeType() {
		return speType;
	}
	public void setSpeType(String speType) {
		this.speType = speType;
	}
	public Date getEffDate() {
		if(effDate != null){
			return (Date) effDate.clone();
		}else{
			return null;			
		}
	}
	public void setEffDate(Date effDate) {
		if(effDate != null){
			this.effDate = (Date) effDate.clone();
		}else{
			this.effDate = null;
		}
	}
	public Date getExpDate() {
		
		if(expDate != null){
			return (Date) expDate.clone();
		}else{
			return null;			
		}

	}
	public void setExpDate(Date expDate) {
		if(expDate != null){
			this.expDate = (Date) expDate.clone();
		}else{
			this.expDate = null;
		}
	}
	@Override
	public String toString() {
		return "SpeUrgeStop [tenantid=" + tenantid + ", ownertype=" + ownertype + ", ownerid=" + ownerid + ", speType="
				+ speType + ", effDate=" + effDate + ", expDate=" + expDate + "]";
	}
	
}
