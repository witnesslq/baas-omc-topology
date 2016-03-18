package com.ai.baas.omc.topoligy.core.manager.parameters.entity;

import java.io.Serializable;

public final class PolicyKey implements Serializable{
	
	private static final long serialVersionUID = 933175119852499692L;

	private String tenantid;
	
	private String policytype;

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	public String getPolicytype() {
		return policytype;
	}

	public void setPolicytype(String policytype) {
		this.policytype = policytype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((policytype == null) ? 0 : policytype.hashCode());
		result = prime * result + ((tenantid == null) ? 0 : tenantid.hashCode());
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
		if (getClass() != obj.getClass()){
			return false;
		}
		PolicyKey other = (PolicyKey) obj;
		if (policytype == null) {
			if (other.policytype != null){
				return false;
			}
		} else if (!policytype.equals(other.policytype)){
			return false;
		}
		
		if (tenantid == null) {
			if (other.tenantid != null){
				return false;
			}
		} else if (!tenantid.equals(other.tenantid)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PolicyKey [tenantid=" + tenantid  + ", policytype=" + policytype + "]";
	}


	
	
}
