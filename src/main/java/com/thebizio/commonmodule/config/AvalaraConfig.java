package com.thebizio.commonmodule.config;

import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvalaraConfig {

    @Value("avalara-app-version")
    private String AVALARA_APP_VERSION;

    @Value("${bizio-env}")
    private String bizioEnv;

    @Value("${avalara-username}")
    private String avalaraUsername;

    @Value("${avalara-password}")
    private String avalaraPassword;

    @Value("${avalara-company-code}")
    private String avalaraCompanyCode;

    private final String AVALARA_MACHINE_NAME = "bizio";

    @Value("${avalara-app-name}")
    private String AVALARA_APP_NAME;

    @Bean
    public AvaTaxClient avaTaxClient(){
        AvaTaxEnvironment envKey = AvaTaxEnvironment.Sandbox;

        if(bizioEnv.equals("production")){
            envKey = AvaTaxEnvironment.Production;
        }

        return new AvaTaxClient(AVALARA_APP_NAME, AVALARA_APP_VERSION, AVALARA_MACHINE_NAME, envKey)
                .withSecurity(avalaraUsername, avalaraPassword);
    }
}
