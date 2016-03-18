package com.ai.baas.omc.topoligy.core.manager.parameters.entity;

import java.io.Serializable;
/**
 * 信控策略参数
 * @author jackieliu
 *
 */
public final class PolicyConf extends BaseConf implements Serializable{
	
	private static final long serialVersionUID = -7424651678156659134L;

	private PolicyConfKey policyConfKey;
	
	private String confvalue;

	public PolicyConfKey getPolicyConfKey() {
		return policyConfKey;
	}

	public void setPolicyConfKey(PolicyConfKey policyConfKey) {
		this.policyConfKey = policyConfKey;
	}

	public String getConfvalue() {
		return confvalue;
	}

	public void setConfvalue(String confvalue) {
		this.confvalue = confvalue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((confvalue == null) ? 0 : confvalue.hashCode());
		result = prime * result + ((policyConfKey == null) ? 0 : policyConfKey.hashCode());
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
		PolicyConf other = (PolicyConf) obj;
		if (confvalue == null) {
			if (other.confvalue != null){
				return false;
			}
		} else if (!confvalue.equals(other.confvalue)){
			return false;
		}
		if (policyConfKey == null) {
			if (other.policyConfKey != null){
				return false;
			}
		} else if (!policyConfKey.equals(other.policyConfKey)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PolicyConf [policyConfKey=" + policyConfKey + ", confvalue=" + confvalue + "]";
	}


}
