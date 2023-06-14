package com.thebizio.commonmodule.service;

import org.springframework.stereotype.Service;

@Service("commonCalculateUtilService")
public class CalculateUtilService {
    public Double roundTwoDigits(Double no){
        return Math.round(no * 100.0) / 100.0;
    }


    public <T> T nullOrZeroValue(T val, T dVal) {
        return val == null ? dVal : val;
    }

    public boolean isEven(int x) { return x % 2 == 0; }
}
