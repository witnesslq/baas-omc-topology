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
        config.put(CacheClient.CACHE_IP,"10.1.241.37");
        config.put(CacheClient.CACHE_PORT,"8686");
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
