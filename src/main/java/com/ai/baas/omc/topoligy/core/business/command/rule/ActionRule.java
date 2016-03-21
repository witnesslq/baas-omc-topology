package com.ai.baas.omc.topoligy.core.business.command.rule;

import com.ai.baas.omc.topoligy.core.constant.SCORULETYPE;
import com.ai.baas.omc.topoligy.core.constant.SERVICESTATUS;
import com.ai.baas.omc.topoligy.core.constant.STATUSCHGTYPE;
import com.ai.baas.omc.topoligy.core.constant.SUBSSTATUS;
import com.ai.baas.omc.topoligy.core.constant.rule.MATCHUSER;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.User;

/**
 * 
* @ClassName: ActionRule 
* @Description: 信控 不生成指令规则
* @author lvsj
* @date 2015年11月5日 下午10:41:37 
*
 */
public final  class ActionRule {
	private ActionRule(){
	}
	public static boolean canAction(String action, User user, ConfigContainer config){
		//TODO:资料匹配
//		config.getCfgPara(parakey, tenantid, policyid, ruleid)
		
//		//是否匹配资料
		String matchUser = ""; // cfg.get("MATCHUSER");
		if ( 1==1 ){
			return true;
		}
		if ((matchUser != null)&&(matchUser.equals(MATCHUSER.NOTMATCH))){
			return true;
		}
		
		if (action.equals(SCORULETYPE.STOP)){
			if (user.getServicestatus().equals(SERVICESTATUS.STOPSERVED)){  //停机
				return false;
			}
			
			if (user.getServicestatus().equals(SERVICESTATUS.SERVED)){
				if (STATUSCHGTYPE.FORCESTART.equals(user.getStatuschgtype())){ //挂失
					return false;
				}
			}
			
		}
		/**
		 * 开机过滤判断
		 */
		if (action.equals(SCORULETYPE.START)){
			
			if (user.getServicestatus().equals(SERVICESTATUS.STOPSERVED)){
				if (STATUSCHGTYPE.FORCESTOP.equals(user.getStatuschgtype())){	// 强停
					return false;
				}
				if (STATUSCHGTYPE.REPORTLOST.equals(user.getStatuschgtype())){ //挂失
					return false;
				}
				if (STATUSCHGTYPE.STOPTOPROTECT.equals(user.getStatuschgtype())){ //停机保号
					return false;
				}
			}
			
			if (user.getSubsstatus().equals(SUBSSTATUS.ADVANCEREMOVE)){ //预销户
				return false;
			}

		}
		if (action.equals(SCORULETYPE.HALFSTOP)){
			if (user.getServicestatus().equals(SERVICESTATUS.STOPSERVED)){  //停机
				return false;
			}
			if (user.getServicestatus().equals(SERVICESTATUS.lIMITSERVED)){  //单停
				return false;
			}
		}
		if (action.equals(SCORULETYPE.WARNING)){
			if (user.getServicestatus().equals(SERVICESTATUS.STOPSERVED)){  //停机
				return false;
			}
			if (user.getServicestatus().equals(SERVICESTATUS.lIMITSERVED)){  //单停
				return false;
			}
		}
		return true;
	}
}
