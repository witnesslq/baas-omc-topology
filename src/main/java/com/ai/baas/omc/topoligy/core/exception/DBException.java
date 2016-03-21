package com.ai.baas.omc.topoligy.core.exception;

/**
 * 数据库异常
 */
public class DBException extends Exception {
	private static final long serialVersionUID = -4985975715070523516L;
	private String code;
	
	public DBException(String code, String msg){
		super(msg);
		this.code = code;
	}
	
	public DBException(String code, String msg, Throwable cause){
		super(msg,cause);
	}
	
	public DBException(String code, Throwable cause){
		super(cause);
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
