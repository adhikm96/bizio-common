package com.thebizio.commonmodule.service;

import net.avalara.avatax.rest.client.models.TransactionSummary;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CalculateUtilService {
    public static Double roundTwoDigits(Double no){
        return Math.round(no * 100.0) / 100.0;
    }


    public static <T> T nullOrZeroValue(T val, T dVal) {
        return val == null ? dVal : val;
    }

    public static boolean isEven(int x) { return x % 2 == 0; }

    public static final DecimalFormat decfor = new DecimalFormat("0.00");

    public static String calculateTaxPercentage(ArrayList<TransactionSummary> transactionSummaries){
        BigDecimal taxPercentage = BigDecimal.valueOf(0);
        for (TransactionSummary ts:transactionSummaries){ taxPercentage = taxPercentage.add(ts.getRate());}
        return decfor.format(taxPercentage.multiply(BigDecimal.valueOf(100)));
    }

}
