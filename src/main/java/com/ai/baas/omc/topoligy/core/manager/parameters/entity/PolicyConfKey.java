package com.ai.baas.omc.topoligy.core.manager.parameters.entity;

import java.io.Serializable;

public final class PolicyConfKey extends BaseConfKey implements Serializable{
	
	private static final long serialVersionUID = 8079458763027138396L;
	private String policyid;

	public String getPolicyid() {
		return policyid;
	}

	public void setPolicyid(String policyid) {
		this.policyid = policyid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((policyid == null) ? 0 : policyid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (!super.equals(obj)){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		PolicyConfKey other = (PolicyConfKey) obj;
		if (policyid == null) {
			if (other.policyid != null){
				return false;
			}
		} else if (!policyid.equals(other.policyid)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PolicyConfKey [policyid=" + policyid + "]";
	}
	
}
