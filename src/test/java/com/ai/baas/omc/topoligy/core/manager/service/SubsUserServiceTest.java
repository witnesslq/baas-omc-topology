package com.ai.baas.omc.topoligy.core.manager.service;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.shm.SubsUserServiceImplShm;
import com.ai.baas.omc.topoligy.core.pojo.User;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jackieliu on 16/3/28.
 */
public class SubsUserServiceTest {

    SubsUserService subsUserService;

    @Before
    public void initCache() throws OmcException {
        Map<String,String> config = new HashMap<>();
        config.put(CacheClient.CACHE_IP,"10.1.241.37");
        config.put(CacheClient.CACHE_PORT,"8686");
        CacheClient.loadResource(config);
        subsUserService = new SubsUserServiceImplShm();
    }

    @Test
    public void selectById(){
        try {
            User user = subsUserService.selectById("VIV-BYD","122601");
            System.out.println(user.toString());
        } catch (OmcException e) {
            e.printStackTrace();
        }
    }
}
