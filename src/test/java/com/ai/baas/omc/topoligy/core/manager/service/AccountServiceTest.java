package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.shm.AccountServiceImplShm;
import com.ai.baas.omc.topoligy.core.pojo.Account;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jackieliu on 16/3/28.
 */
public class AccountServiceTest {
    AccountService accountService;
    @Before
    public void initCacheClient() throws OmcException {
        Map<String,String> config = new HashMap<>();
        config.put(CacheClient.PAAS_AUTH_URL,"http://10.1.245.4:19811/service-portal-uac-web/service/auth");
        config.put(CacheClient.PAAS_AUTH_PID,"87EA5A771D9647F1B5EBB600812E3067");
        config.put(CacheClient.PAAS_CCS_SERVICEID,"CCS008");
        config.put(CacheClient.PAAS_CCS_SERVICE_PASSWORD,"123456");
        CacheClient.loadResource(config);
        accountService = new AccountServiceImplShm();
    }

    @Test
    public void selectByIdTest() {
        try {
            Account account = accountService.selectById("VIV-BYD", "122301");
            System.out.print(account.toString());
        } catch (OmcException e) {
            e.printStackTrace();
        }
    }

}
