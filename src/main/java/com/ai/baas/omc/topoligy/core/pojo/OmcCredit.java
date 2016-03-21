package com.ai.baas.omc.topoligy.core.pojo;

import java.util.Date;

/**
 * 信用度
 */
public final class OmcCredit {
	
	private String 	tenantid;
	private String ownerid;
	private String ownertype;
	private String credittype;
	private double creditline;
	private Date	effDate;
	private Date	expDate;
	
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}
	public String getOwnertype() {
		return ownertype;
	}
	public void setOwnertype(String ownertype) {
		this.ownertype = ownertype;
	}
	public String getCredittype() {
		return credittype;
	}
	public void setCredittype(String credittype) {
		this.credittype = credittype;
	}
	public double getCreditline() {
		return creditline;
	}
	public void setCreditline(double creditline) {
		this.creditline = creditline;
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
		return "OmcCredit [tenantid=" + tenantid + ", ownerid=" + ownerid + ", ownertype=" + ownertype + ", credittype="
				+ credittype + ", creditline=" + creditline + ", effDate=" + effDate + ", expDate=" + expDate + "]";
	}
    
	
}
