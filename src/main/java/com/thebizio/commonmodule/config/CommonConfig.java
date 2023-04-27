package com.thebizio.commonmodule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebizio.commonmodule.service.*;
import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CommonConfig {

    @Value("app-version")
    private String APP_VERSION;

    @Value("${bizio-center-env}")
    private String bizioCenterEnv;

    @Value("${avalara-username}")
    private String avalaraUsername;

    @Value("${avalara-password}")
    private String avalaraPassword;

    @Value("${avalara-company-code}")
    private String avalaraCompanyCode;

    @Value("${machine-name}")
    private String MACHINE_NAME;

    @Value("${app-name}")
    private String APP_NAME;

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

        return new AvaTaxClient(APP_NAME, APP_VERSION, MACHINE_NAME, envKey)
                .withSecurity(avalaraUsername, avalaraPassword);
    }
}
