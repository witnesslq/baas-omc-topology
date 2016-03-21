package com.ai.baas.omc.topoligy.core.manager.service.shm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.CustomerService;
import com.ai.baas.omc.topoligy.core.pojo.Customer;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import org.apache.commons.lang.StringUtils;

public final class CustomerServiceImplShm implements CustomerService {
	
	private  static final CacheClient cacheClient = CacheClient.getInstance();
	
	@Override
	public Customer getCustomer(String tenantid, String custId) throws OmcException {
		try{
			StringBuilder table = new StringBuilder();
			
			table.append("bl_custinfo");
			
			Map<String, String> params = new TreeMap<String, String>();
			
			params.put("CUST_ID",custId);
			params.put("TENANT_ID",tenantid);


			List<Map<String, String>> result = cacheClient.doQuery(table.toString(), params);
			
			if(result == null || result.size()==0){
				throw new OmcException("OMC-SUBS0001B","bl_custinfo表没有找到客户信息!" + params.toString());
			}
			
		   return  getCustomers(result).get(0);

		}catch (Exception e){
			throw new OmcException("OMC-SUBS0001B",e);
		}
	}
	
	private List<Customer> getCustomers(List<Map<String, String>> result){
		
		String[] custid =	StringUtils.split(result.get(0).get("cust_id"),"#");
		String[] custleve =	StringUtils.split(result.get(0).get("cust_grade"),"#");
		String[] custtype =	StringUtils.split(result.get(0).get("cust_type"),"#");
		String[] tenantid = StringUtils.split(result.get(0).get("tenant_id"),"#");
		String[] systemid = StringUtils.split(result.get(0).get("system_id"),"#");
		   
		List<Customer> customers = new ArrayList<Customer>();
		
		for (int i = 0; i < custid.length; i++) {
			Customer customer = new Customer();
			customer.setCustomerId(custid[i]);
			customer.setCustType(custtype[i]);
			customer.setCustLevel(custleve[i]);
			customer.setSystemId(systemid[i]);
			customer.setTenantId(tenantid[i]);
			customers.add(customer);
		}
		
		return customers;
	}

}
