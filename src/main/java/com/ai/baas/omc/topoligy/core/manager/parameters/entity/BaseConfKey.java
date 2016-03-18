package com.ai.baas.omc.topoligy.core.manager.parameters.entity;

import java.io.Serializable;

public class BaseConfKey implements Serializable{

	private static final long serialVersionUID = 4619819192872209005L;
	
	
	private String tenantid;
	
	private String confkey;
	
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getConfkey() {
		return confkey;
	}
	public void setConfkey(String confkey) {
		this.confkey = confkey;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((confkey == null) ? 0 : confkey.hashCode());
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
		BaseConfKey other = (BaseConfKey) obj;
		if (confkey == null) {
			if (other.confkey != null){
				return false;
			}
		} else if (!confkey.equals(other.confkey)){
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
		return "BaseConfkey [ tenantid=" + tenantid + ", confkey=" + confkey + "]";
	}

	
}
