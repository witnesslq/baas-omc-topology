package com.ai.baas.omc.topoligy.core.exception;

public class OmcException extends Exception {
	private static final long serialVersionUID = -4985975715070523516L;
	private String code;
	
	public OmcException(String code, String msg){
		super(msg);
		this.code = code;
	}
	
	public OmcException(String code, String msg, Throwable cause){
		super(msg,cause);
	}
	
	public OmcException(String code, Throwable cause){
		super(cause);
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
