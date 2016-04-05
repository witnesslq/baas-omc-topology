package com.ai.baas.omc.topoligy.core.dubbo.config;

import com.ai.baas.omc.topoligy.core.util.DubboxUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dubbox的配置类
 * Created by jackieliu on 16/3/29.
 */
@Configuration
public class ConsumerConfig {
    private static Logger LOGGER = LoggerFactory.getLogger(ConsumerConfig.class);
    public static final String APPLICATION_NAME = "dubbo.app.name";
    //注册中心地址
    public static final String REGISTRY_ADDRESS = "dubbo.reg.adds";
    //服务交互协议
    public static final String DUBBO_PROTOCOL = "dubbo.protocol";
    //扫描包
    public static final String ANNOTATION_PACKAGE = "com.ai.baas.omc.topoligy.core.dubbo.service";
    @Bean
    public ApplicationConfig applicationConfig() {
        String appName = DubboxUtils.getConfigByName(APPLICATION_NAME);
        LOGGER.info("The appName is "+appName);
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(appName);
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        String regAddress = DubboxUtils.getConfigByName(REGISTRY_ADDRESS);
        LOGGER.info("The registry address is "+regAddress);
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(regAddress);
        registryConfig.setProtocol(DubboxUtils.getConfigByName(DUBBO_PROTOCOL));
        return registryConfig;
    }

    @Bean
    public AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage(ANNOTATION_PACKAGE);
        return annotationBean;
    }
}
