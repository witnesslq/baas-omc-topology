package com.ai.baas.omc.topoligy.core.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * Created by jackieliu on 16/3/29.
 */
public class DubboxUtils {

    static Map<String,String> DUBBOX_CONFIG;
    static AnnotationConfigApplicationContext context;
    //javaConfig包
    static final String CONFIG_PATH = "com.ai.baas.omc.topoligy.core.dubbo.config";

    /**
     * 初始化相关参数
     */
    public static void initContext(Map<String, String> conf){
        DUBBOX_CONFIG = conf;
        context = new AnnotationConfigApplicationContext(CONFIG_PATH);
        context.start();
    }

    /**
     * 获取指定配置信息
     * @param confName
     * @return
     */
    public static String getConfigByName(String confName){
        if (StringUtils.isBlank(confName)){
            return null;
        }
        return DUBBOX_CONFIG.get(confName);
    }


}
