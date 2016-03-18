package com.ai.baas.omc.topoligy.core.manager.parameters.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 信控策略
 * @author jackieliu
 *
 */
public class Policy implements Serializable{
	
	private static final long serialVersionUID = -9199269140006884007L;

	private PolicyKey policyKey;
	
	private String policyId;

	private String policyDescribe;
	
	private String status;
	
	private Date effdate;
	
	private Date expdate;

	public PolicyKey getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(PolicyKey policyKey) {
		this.policyKey = policyKey;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getPolicyDescribe() {
		return policyDescribe;
	}

	public void setPolicyDescribe(String policyDescribe) {
		this.policyDescribe = policyDescribe;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEffdate() {
		if(effdate != null){
			return (Date) effdate.clone();
		}else{
			return null;			
		}
	}

	public void setEffdate(Date effdate) {
		if(effdate != null){
			this.effdate = (Date) effdate.clone();
		}else{
			this.effdate = null;
		}
	}

	public Date getExpdate() {
		if(expdate != null){
			return (Date) expdate.clone();
		}else{
			return null;			
		}
	}

	public void setExpdate(Date expdate) {
		if(expdate != null){
			this.expdate = (Date) expdate.clone();
		}else{
			this.expdate = null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((effdate == null) ? 0 : effdate.hashCode());
		result = prime * result + ((expdate == null) ? 0 : expdate.hashCode());
		result = prime * result + ((policyDescribe == null) ? 0 : policyDescribe.hashCode());
		result = prime * result + ((policyId == null) ? 0 : policyId.hashCode());
		result = prime * result + ((policyKey == null) ? 0 : policyKey.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Policy other = (Policy) obj;
		if (effdate == null) {
			if (other.effdate != null){
				return false;
			}
		} else if (!effdate.equals(other.effdate)){
			return false;
		}
		if (expdate == null) {
			if (other.expdate != null){
				return false;
			}
		} else if (!expdate.equals(other.expdate)){
			return false;
		}
		if (policyDescribe == null) {
			if (other.policyDescribe != null){
				return false;
			}
		} else if (!policyDescribe.equals(other.policyDescribe)){
			return false;
		}
		if (policyId == null) {
			if (other.policyId != null){
				return false;
			}
		} else if (!policyId.equals(other.policyId)){
			return false;
		}
		if (policyKey == null) {
			if (other.policyKey != null){
				return false;
			}
		} else if (!policyKey.equals(other.policyKey)){
			return false;
		}
		if (status == null) {
			if (other.status != null){
				return false;
			}
		} else if (!status.equals(other.status)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Policy [policyKey=" + policyKey + ", policyId=" + policyId + ", policyDescribe=" + policyDescribe
				+ ", status=" + status + ", effdate=" + effdate + ", expdate=" + expdate + "]";
	}



}
