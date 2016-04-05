package com.ai.baas.omc.topoligy.core.util;

import com.ai.baas.omc.topoligy.core.dubbo.service.RealtimeBalanceService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * dubbo服务初始化
 * Created by jackieliu on 16/3/29.
 */
public class DubboxUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(DubboxUtils.class);
    static Map<String,String> DUBBOX_CONFIG;
    static AnnotationConfigApplicationContext context;
    //javaConfig包
    static final String CONFIG_PATH = "com.ai.baas.omc.topoligy.core.dubbo.config";

    /**
     * 初始化相关参数
     */
    public static void initContext(Map<String, String> conf){
        LOGGER.info("start init context");
        DUBBOX_CONFIG = conf;
        context = new AnnotationConfigApplicationContext(CONFIG_PATH);
        context.start();
        LOGGER.info("start init context");
    }

    /**
     * 获取指定配置信息
     * @param confName
     * @return
     */
    public static String getConfigByName(String confName){
        if (StringUtils.isBlank(confName)){
            LOGGER.error("configuration has no config:"+confName);
            return null;
        }
        return DUBBOX_CONFIG.get(confName);
    }

    public static RealtimeBalanceService getBalanceService(){
        return context.getBean(RealtimeBalanceService.class);
    }

}
