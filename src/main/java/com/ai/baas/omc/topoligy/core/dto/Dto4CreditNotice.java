package com.ai.baas.omc.topoligy.core.dto;

import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.RealTimeBalance;

import java.io.Serializable;

/**
 * 信控通知所需参数
 *
 */
public final class Dto4CreditNotice implements Serializable {

	private static final long serialVersionUID = 8735911506473021573L;
	private OmcObj owner;
	private RealTimeBalance realTimeBalance;
	private String extInfo;

	
	public OmcObj getOwner() {
		return owner;
	}
	public void setOwner(OmcObj owner) {
		this.owner = owner;
	}
	

	public RealTimeBalance getRealTimeBalance() {
		return realTimeBalance;
	}
	public void setRealTimeBalance(RealTimeBalance realTimeBalance) {
		this.realTimeBalance = realTimeBalance;
	}
	public String getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	@Override
	public String toString() {
		return "Dto4CreditNotice [owner=" + owner 
				+ ", realTimeBalance=" + realTimeBalance + ", extInfo=" + extInfo + "]";
	}    
	
}
