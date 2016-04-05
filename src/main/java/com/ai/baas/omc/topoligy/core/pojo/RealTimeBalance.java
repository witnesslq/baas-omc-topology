package com.ai.baas.omc.topoligy.core.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 实时余额信息
 */
public final class RealTimeBalance implements Serializable {
	private static final long serialVersionUID = 690621398847099331L;
	private OmcObj owner;
	private BigDecimal realBalance;
	private BigDecimal realBill;
	private BigDecimal balance;
	private BigDecimal unSettleBill;
	private BigDecimal unIntoBill;
	private String fstUnSettleMon;
	private int unSettleMons;
	private BigDecimal creditline;
	private String acctMonth;
	private String  extInfo;
	public OmcObj getOwner() {
		return owner;
	}
	public void setOwner(OmcObj owner) {
		this.owner = owner;
	}
	public BigDecimal getRealBalance() {
		return realBalance;
	}
	public void setRealBalance(BigDecimal realBalance) {
		this.realBalance = realBalance;
	}
	public BigDecimal getRealBill() {
		return realBill;
	}
	public void setRealBill(BigDecimal realBill) {
		this.realBill = realBill;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getUnSettleBill() {
		return unSettleBill;
	}
	public void setUnSettleBill(BigDecimal unSettleBill) {
		this.unSettleBill = unSettleBill;
	}
	public BigDecimal getUnIntoBill() {
		return unIntoBill;
	}
	public void setUnIntoBill(BigDecimal unIntoBill) {
		this.unIntoBill = unIntoBill;
	}
	public String getFstUnSettleMon() {
		return fstUnSettleMon;
	}
	public void setFstUnSettleMon(String fstUnSettleMon) {
		this.fstUnSettleMon = fstUnSettleMon;
	}
	public int getUnSettleMons() {
		return unSettleMons;
	}
	public void setUnSettleMons(int unSettleMons) {
		this.unSettleMons = unSettleMons;
	}
	public BigDecimal getCreditline() {
		return creditline;
	}
	public void setCreditline(BigDecimal creditline) {
		this.creditline = creditline;
	}
	public String getAcctMonth() {
		return acctMonth;
	}
	public void setAcctMonth(String acctMonth) {
		this.acctMonth = acctMonth;
	}
	public String getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	@Override
	public String toString() {
		return "RealTimeBalance [owner=" + owner +  ", realBalance=" + realBalance
				+ ", realBill=" + realBill + ", balance=" + balance + ", unSettleBill=" + unSettleBill + ", unIntoBill="
				+ unIntoBill + ", fstUnSettleMon=" + fstUnSettleMon + ", UnSettleMons=" + unSettleMons + ", creditline="
				+ creditline + ", acctMonth=" + acctMonth + ", extInfo=" + extInfo + "]";
	}

	


}