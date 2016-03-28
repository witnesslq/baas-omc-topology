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
        config.put(CacheClient.CACHE_IP,"10.1.241.37");
        config.put(CacheClient.CACHE_PORT,"8686");
        CacheClient.loadResource(config);
        accountService = new AccountServiceImplShm();
    }

    @Test
    public void selectByIdTest() {
        try {
            Account account = accountService.selectById("VIV-BYD", "10000002");
            System.out.print(account.toString());
        } catch (OmcException e) {
            e.printStackTrace();
        }
    }

}
