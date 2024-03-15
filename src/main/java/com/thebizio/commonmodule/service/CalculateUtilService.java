package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CalculateUtilService {
    public static final String DEVELOPMENT_ENV = "development";

    public static Double roundTwoDigits(Double no){
        return Math.round(no * 100.0) / 100.0;
    }

    public static <T> T nullOrZeroValue(T val, T dVal) {
        return val == null ? dVal : val;
    }

    public static boolean isEven(int x) { return x % 2 == 0; }

    public static final DecimalFormat decfor = new DecimalFormat("0.00");

    public static String calculateTaxPercentage(JsonNode summary){
        if(!summary.has("combinedTaxRate")) return "0";
        BigDecimal taxPercentage = BigDecimal.valueOf(Double.parseDouble(summary.get("combinedTaxRate").asText()));
        return decfor.format(taxPercentage.multiply(BigDecimal.valueOf(100)));
    }

    public static boolean isDevEnv() {
        return System.getenv("BIZIO_ENV") != null && System.getenv("BIZIO_ENV").equals(DEVELOPMENT_ENV);
    }
}
