package com.ai.baas.omc.topoligy.core.constant;
/**
 * 
* @ClassName: SUBSSTATUS 
* @Description: 用户订购状态 
* @author lvsj
* @date 2015年11月5日 下午5:55:01 
*
 */
public interface SubsStatus {
	/**
	 * 1 正式开户
	 */
	String OPEN = "1";
	/**
	 *	2：预开户（对应于移动业务转正之前的状态，对应于固网业务竣工之前的状态） 
	 */
	String ADVANCEOPEN = "2";
	/**
	 * 3：暂开户
	 */
	String TEMPORARYOPEN = "3";	
	/**
	 * 4：预约销户
	 */
	String ADVANCEREMOVE = "4";
	/**
	 * 5. 正式销户
	 */
	String REMOVE = "5";
	/**
	 * 6. 立即销户
	 */
	String IMMEDIATELYREMOVE = "6";
	/**
	 * 7. 强制销户
	 */
	String FORCEREMOVE  = "7";
	/**
	 * 8. 妥投转拒收销户
	 */
	String OTHERREMOVE = "8";
	
	
}
