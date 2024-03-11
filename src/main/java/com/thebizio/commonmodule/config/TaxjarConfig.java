package com.thebizio.commonmodule.config;

import com.taxjar.Taxjar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TaxjarConfig {
    private final String taxJarApiToken;

    private final String taxJarApiVersion;

    public TaxjarConfig(@Value("${taxjar-api-token}") String taxJarApiToken,@Value("${taxjar-api-version}") String taxJarApiVersion) {
        this.taxJarApiToken = taxJarApiToken;
        this.taxJarApiVersion = taxJarApiVersion;
    }

    @Bean
    public Taxjar taxjar() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-api-version", taxJarApiVersion);
        return new Taxjar(taxJarApiToken, params);
    }
}
