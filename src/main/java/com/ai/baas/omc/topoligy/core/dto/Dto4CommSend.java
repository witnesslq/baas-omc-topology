package com.ai.baas.omc.topoligy.core.dto;

import com.ai.baas.omc.topoligy.core.pojo.OmcBmsInterface;

import java.io.Serializable;
import java.util.List;

public final class Dto4CommSend implements Serializable{

	private static final long serialVersionUID = -5688850153547979946L;
	
	List<OmcBmsInterface>  omcBmsInterfaces;

	/**
	 * @return the omcBmsInterfaces
	 */
	public List<OmcBmsInterface> getOmcBmsInterfaces() {
		return omcBmsInterfaces;
	}

	/**
	 * @param omcBmsInterfaces the omcBmsInterfaces to set
	 */
	public void setOmcBmsInterfaces(List<OmcBmsInterface> omcBmsInterfaces) {
		this.omcBmsInterfaces = omcBmsInterfaces;
	}

	@Override
	public String toString() {
		return "Dto4CommSend [" + (omcBmsInterfaces != null ? "omcBmsInterfaces=" + omcBmsInterfaces : "") + "]";
	}
	
	
}
