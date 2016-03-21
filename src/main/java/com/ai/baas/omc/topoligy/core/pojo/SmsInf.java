package com.ai.baas.omc.topoligy.core.pojo;

import java.sql.Timestamp;

/**
 * 短信信息接口
 */
public final class SmsInf  {
	private long serialno;
	private String tenantid;
	private String systemid;
	private String ownertype;
	private String ownerid;
	private String urgeinfo;
	private Timestamp inserttime;
	private int retrytime;
	private Timestamp dealtime;
	private int dealflag;
	private String remark;

	public long getSerialno() {
		return serialno;
	}
	public void setSerialno(long serialno) {
		this.serialno = serialno;
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
	public String getUrgeinfo() {
		return urgeinfo;
	}
	public void setUrgeinfo(String urgeinfo) {
		this.urgeinfo = urgeinfo;
	}
	public Timestamp getInserttime() {
		if(inserttime != null){
			return (Timestamp) inserttime.clone();
		}else{
			return null;			
		}
		
	}
	public void setInserttime(Timestamp inserttime) {
		if(inserttime != null){
			this.inserttime = (Timestamp) inserttime.clone();
		}else{
			this.inserttime = null;
		}

	}
	public int getRetrytime() {
		return retrytime;
	}
	public void setRetrytime(int retrytime) {
		this.retrytime = retrytime;
	}
	public Timestamp getDealtime() {

		if(dealtime != null){
			return (Timestamp) dealtime.clone();
		}else{
			return null;			
		}
		
	}
	public void setDealtime(Timestamp dealtime) {

		if(dealtime != null){
			this.dealtime = (Timestamp) dealtime.clone();
		}else{
			this.dealtime = null;
		}
	}
	public int getDealflag() {
		return dealflag;
	}
	public void setDealflag(int dealflag) {
		this.dealflag = dealflag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "SmsInf [serialno=" + serialno + ", tenantid=" + tenantid + ", systemid=" + systemid + ", ownertype="
				+ ownertype + ", ownerid=" + ownerid + ", urgeinfo=" + urgeinfo + ", inserttime=" + inserttime
				+ ", retrytime=" + retrytime + ", dealtime=" + dealtime + ", dealflag=" + dealflag + ", remark="
				+ remark + "]";
	}
	
}
