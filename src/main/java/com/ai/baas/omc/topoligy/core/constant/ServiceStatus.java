package com.ai.baas.omc.topoligy.core.constant;
/**
 * 
* @ClassName: ServiceStatus 
* @Description: 用户服务状态 
* @author lvsj
* @date 2015年10月27日 上午4:02:15 
*
 */
public interface ServiceStatus {
	/**
	 * 失效 0
	 */
	 String OUTSERVED  = "0";
	/**
	 * 正常服务 1
	 */
	 String SERVED  = "1";
	/**
	 * 停机双停 2
	 */
	 String STOPSERVED  = "2";
	/**
	 * 限制服务，单停 3
	 */
	 String lIMITSERVED = "3";
	/**
	 * 销户 4
	 */
	 String REMOVESERVED = "4";
}
