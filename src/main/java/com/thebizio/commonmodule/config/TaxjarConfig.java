package com.thebizio.commonmodule.config;

import com.taxjar.Taxjar;
import com.thebizio.commonmodule.service.CalculateUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
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

        if(CalculateUtilService.isDevEnv()) {
            params.put("apiUrl", Taxjar.SANDBOX_API_URL);
            log.info("using sandbox for taxjar api");
        }

        return new Taxjar(taxJarApiToken, params);
    }
}
