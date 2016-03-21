package com.ai.baas.omc.topoligy.core.business.command.rule;

import com.ai.baas.omc.topoligy.core.constant.ScoRuleType;
import com.ai.baas.omc.topoligy.core.constant.ServiceStatus;
import com.ai.baas.omc.topoligy.core.constant.StatusChgType;
import com.ai.baas.omc.topoligy.core.constant.SubsStatus;
import com.ai.baas.omc.topoligy.core.constant.rule.MatchUser;
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
		if ((matchUser != null)&&(matchUser.equals(MatchUser.NOTMATCH))){
			return true;
		}
		
		if (action.equals(ScoRuleType.STOP)){
			if (user.getServicestatus().equals(ServiceStatus.STOPSERVED)){  //停机
				return false;
			}
			
			if (user.getServicestatus().equals(ServiceStatus.SERVED)){
				if (StatusChgType.FORCESTART.equals(user.getStatuschgtype())){ //挂失
					return false;
				}
			}
			
		}
		/**
		 * 开机过滤判断
		 */
		if (action.equals(ScoRuleType.START)){
			
			if (user.getServicestatus().equals(ServiceStatus.STOPSERVED)){
				if (StatusChgType.FORCESTOP.equals(user.getStatuschgtype())){	// 强停
					return false;
				}
				if (StatusChgType.REPORTLOST.equals(user.getStatuschgtype())){ //挂失
					return false;
				}
				if (StatusChgType.STOPTOPROTECT.equals(user.getStatuschgtype())){ //停机保号
					return false;
				}
			}
			
			if (user.getSubsstatus().equals(SubsStatus.ADVANCEREMOVE)){ //预销户
				return false;
			}

		}
		if (action.equals(ScoRuleType.HALFSTOP)){
			if (user.getServicestatus().equals(ServiceStatus.STOPSERVED)){  //停机
				return false;
			}
			if (user.getServicestatus().equals(ServiceStatus.lIMITSERVED)){  //单停
				return false;
			}
		}
		if (action.equals(ScoRuleType.WARNING)){
			if (user.getServicestatus().equals(ServiceStatus.STOPSERVED)){  //停机
				return false;
			}
			if (user.getServicestatus().equals(ServiceStatus.lIMITSERVED)){  //单停
				return false;
			}
		}
		return true;
	}
}
