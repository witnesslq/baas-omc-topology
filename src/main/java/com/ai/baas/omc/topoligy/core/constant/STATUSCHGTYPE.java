package com.ai.baas.omc.topoligy.core.constant;

public interface StatusChgType {
	/**
	 *	0	新装 
	 */
	 String NEW ="0";
	/**
	 *  1	报开(解挂) 
	 */
	 String REPORTOPEN = "1";
	/**
	 * 	6	报停(挂失)
	 */
	 String REPORTLOST = "6";
	/**
	 * 	8	预约销户
	 */
	 String ADVANCEREMOVE = "8";
	/**
	 * 	9	预约销户撤销
	 */
	 String UNADVANCEREMOVE = "9";
	/**
	 * 	5	强停 
	 */
	 String FORCESTOP = "5";
	/**
	 * 	3	强开
	 */
	 String FORCESTART = "3";
	/**
	 *	10	销户 
	 */
	 String REMOVE = "10";
	/**
	 *	12	强制销户 
	 */
	 String FORCEREMOVE = "12";
	/**
	 *  	34	停机保号 
	 */
	 String STOPTOPROTECT = "34";	
	/**
	 *  	35	复机 
	 */
	 String REOPEN = "35";	
	/**
	 *	     2	帐务开机(清欠自动开机) 
	 */
	 String OPEN = "3";	
	/**
	 *		4	帐务欠停(欠费全停) 
	 */
	 String CLOSE = "4";	
	/**
	 *		7	帐务欠半停(欠费半停) 
	 */
	/**
	 *		50            预开 
	 */

	/**
	 *		51            妥投转拒收销户 
	 */
	/**
	 *		52            过账户 
	 */
}
