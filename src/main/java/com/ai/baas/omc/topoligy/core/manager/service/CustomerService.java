package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.Customer;

/**
 * 
* @ClassName: CustomerService 
* @Description: 利用的客户ID得到客户信息 
* @author lvsj
* @date 2015年11月24日 下午4:49:24 
*
 */
public interface CustomerService {

	Customer getCustomer(String tenantid, String custId) throws OmcException;
}
