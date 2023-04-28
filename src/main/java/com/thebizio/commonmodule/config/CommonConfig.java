package com.thebizio.commonmodule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Value("avalara-app-version")
    private String AVALARA_APP_VERSION;

    @Value("${avalara-bizio-env}")
    private String bizioCenterEnv;

    @Value("${avalara-username}")
    private String avalaraUsername;

    @Value("${avalara-password}")
    private String avalaraPassword;

    @Value("${avalara-company-code}")
    private String avalaraCompanyCode;

    @Value("${avalara-machine-name}")
    private String AVALARA_MACHINE_NAME;

    @Value("${avalara-app-name}")
    private String AVALARA_APP_NAME;

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AvaTaxClient avaTaxClient(){
        AvaTaxEnvironment envKey = AvaTaxEnvironment.Sandbox;

        if(bizioCenterEnv.equals("production")){
            envKey = AvaTaxEnvironment.Production;
        }

        return new AvaTaxClient(AVALARA_APP_NAME, AVALARA_APP_VERSION, AVALARA_MACHINE_NAME, envKey)
                .withSecurity(avalaraUsername, avalaraPassword);
    }
}
