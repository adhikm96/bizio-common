package com.thebizio.commonmodule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
