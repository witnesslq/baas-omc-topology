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
        config.put(CacheClient.PAAS_AUTH_URL,"http://10.1.245.4:19811/service-portal-uac-web/service/auth");
        config.put(CacheClient.PAAS_AUTH_PID,"87EA5A771D9647F1B5EBB600812E3067");
        config.put(CacheClient.PAAS_CCS_SERVICEID,"CCS008");
        config.put(CacheClient.PAAS_CCS_SERVICE_PASSWORD,"123456");
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
