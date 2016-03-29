package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.shm.CustomerServiceImplShm;
import com.ai.baas.omc.topoligy.core.pojo.Customer;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jackieliu on 16/3/28.
 */
public class CustomerServiceTest {

    CustomerService customerService;
    @Before
    public void initCacheClient() throws OmcException {
        Map<String,String> config = new HashMap<>();
        config.put(CacheClient.CCS_APP_NAME,"aiopt-baas-dshm");
        config.put(CacheClient.CCS_ZK_ADDRESS,"10.1.130.84:39181");
        CacheClient.loadResource(config);
        customerService = new CustomerServiceImplShm();
    }

    @Test
    public void getCustomerTest(){
        try {
            Customer customer = customerService.getCustomer("VIV-BYD","122601");
            System.out.println(customer.toString());
        } catch (OmcException e) {
            e.printStackTrace();
        }
    }

}
