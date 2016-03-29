package com.ai.baas.omc.topoligy.core.dubbo.config;

import com.ai.baas.omc.topoligy.core.util.DubboxUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jackieliu on 16/3/29.
 */
@Configuration
public class ConsumerConfig {

    public static final String APPLICATION_NAME = "dubbox.app.name";

    public static final String REGISTRY_ADDRESS = "dubbox.reg.adds";

    public static final String ANNOTATION_PACKAGE = "com.alibaba.dubbo.demo.consumer";
    @Bean
    public ApplicationConfig applicationConfig() {
        String appName = DubboxUtils.getConfigByName(APPLICATION_NAME);
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(appName);
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(REGISTRY_ADDRESS);
        return registryConfig;
    }

    @Bean
    public AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage(ANNOTATION_PACKAGE);
        return annotationBean;
    }
}
