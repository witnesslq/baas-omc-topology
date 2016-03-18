package com.ai.baas.omc.topoligy.core.manager.parameters.entity;

import java.io.Serializable;

public class BaseConf implements Serializable{
	
	private static final long serialVersionUID = -275813087001022344L;

	private BaseConfKey confkey;
	
	private String confvalue;

	public BaseConfKey getConfkey() {
		return confkey;
	}

	public void setConfkey(BaseConfKey confkey) {
		this.confkey = confkey;
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
		int result = 1;
		result = prime * result + ((confkey == null) ? 0 : confkey.hashCode());
		result = prime * result + ((confvalue == null) ? 0 : confvalue.hashCode());
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
		BaseConf other = (BaseConf) obj;
		if (confkey == null) {
			if (other.confkey != null){
				return false;
			}
		} else if (!confkey.equals(other.confkey)){
			return false;
		}
		if (confvalue == null) {
			if (other.confvalue != null){
				return false;
			}
		} else if (!confvalue.equals(other.confvalue)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BaseConf [confkey=" + confkey + ", confvalue=" + confvalue + "]";
	}
	
}
