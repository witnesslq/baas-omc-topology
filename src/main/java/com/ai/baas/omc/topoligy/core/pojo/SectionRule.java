package com.ai.baas.omc.topoligy.core.pojo;
/**
 * 信控规则
 * @author jackieliu
 *
 */
public final class SectionRule {

	private String policyId;
	private String tenantid;
	private int scoutruleid;
	private String scouttype;
	private String sectiontype;
	private Long balancefloor;
	private Long balanceceil;
	private int owemaxdays;
	private int owemindays;
	private Long chargeceil;
	private Long chargefloor;
	private String custtype;
	private String accttype;
	private String usertype;
	private String custlevel;
	
	public String getSectiontype() {
		return sectiontype;
	}
	public void setSectiontype(String sectiontype) {
		this.sectiontype = sectiontype;
	}
	public String getPolicyId() {
		return policyId;
	}
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public int getScoutruleid() {
		return scoutruleid;
	}
	public void setScoutruleid(int scoutruleid) {
		this.scoutruleid = scoutruleid;
	}
	public String getScouttype() {
		return scouttype;
	}
	public void setScouttype(String scouttype) {
		this.scouttype = scouttype;
	}
	public Long getBalancefloor() {
		return balancefloor;
	}
	public void setBalancefloor(Long balancefloor) {
		this.balancefloor = balancefloor;
	}
	public Long getBalanceceil() {
		return balanceceil;
	}
	public void setBalanceceil(Long balanceceil) {
		this.balanceceil = balanceceil;
	}
	public int getOwemaxdays() {
		return owemaxdays;
	}
	public void setOwemaxdays(int owemaxdays) {
		this.owemaxdays = owemaxdays;
	}
	public int getOwemindays() {
		return owemindays;
	}
	public void setOwemindays(int owemindays) {
		this.owemindays = owemindays;
	}
	public Long getChargeceil() {
		return chargeceil;
	}
	public void setChargeceil(Long chargeceil) {
		this.chargeceil = chargeceil;
	}
	public Long getChargefloor() {
		return chargefloor;
	}
	public void setChargefloor(Long chargefloor) {
		this.chargefloor = chargefloor;
	}
	public String getCusttype() {
		return custtype;
	}
	public void setCusttype(String custtype) {
		this.custtype = custtype;
	}
	public String getAccttype() {
		return accttype;
	}
	public void setAccttype(String accttype) {
		this.accttype = accttype;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getCustlevel() {
		return custlevel;
	}
	public void setCustlevel(String custlevel) {
		this.custlevel = custlevel;
	}
	@Override
	public String toString() {
		return "SectionRule [policyId=" + policyId + ", tenantid=" + tenantid + ", scoutruleid=" + scoutruleid
				+ ", scouttype=" + scouttype + ", sectiontype=" + sectiontype + ", balancefloor=" + balancefloor
				+ ", balanceceil=" + balanceceil + ", owemaxdays=" + owemaxdays + ", owemindays=" + owemindays
				+ ", chargeceil=" + chargeceil + ", chargefloor=" + chargefloor + ", custtype=" + custtype
				+ ", accttype=" + accttype + ", usertype=" + usertype + ", custlevel=" + custlevel + "]";
	}


}
