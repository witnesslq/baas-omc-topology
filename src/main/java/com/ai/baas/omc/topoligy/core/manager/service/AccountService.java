package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.Account;

import java.util.List;

/**
 * 
* @ClassName: AccountService 
* @Description: 得到账户信息
* @author lvsj
* @date 2015年11月24日 下午5:13:27 
*
 */
public interface AccountService {
	Account selectById(String tenantid, String acctId) throws OmcException;
	List<Account> selectBycustId(String tenantid, String custId) throws OmcException;
}