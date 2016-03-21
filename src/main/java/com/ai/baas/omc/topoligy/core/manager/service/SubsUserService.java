package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.User;

import java.util.List;

/**
 * 
* @ClassName: SubsUserService 
* @Description: 得到用户信息 
* @author lvsj
* @date 2015年11月24日 下午4:52:34 
*
 */
public interface SubsUserService {
	User selectById(String tenantid, String id) throws OmcException;
	List<User> selectByAcctId(String tenantid, String id) throws OmcException;
	List<User>  selectByCustId(String tenantid, String id) throws OmcException;
	User selectByNbr(String tenantid, String nbr) throws OmcException;

}
