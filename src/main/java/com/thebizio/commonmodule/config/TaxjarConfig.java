package com.thebizio.commonmodule.config;

import com.taxjar.Taxjar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.thebizio.commonmodule.service.tax.TaxJarService.DEVELOPMENT_ENV;

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

        if(System.getenv("BIZIO_ENV") != null && System.getenv("BIZIO_ENV").equals(DEVELOPMENT_ENV)) {
            params.put("apiUrl", Taxjar.SANDBOX_API_URL);
            log.info("using sandbox for taxjar api");
        }
        return new Taxjar(taxJarApiToken, params);
    }
}
