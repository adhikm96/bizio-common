package com.thebizio.commonmodule.exception;

public class TaxCalculationException extends Throwable {
    public TaxCalculationException(Exception exception) {
        super(exception);
    }

    public TaxCalculationException(String msg) {
        super(msg);
    }
}
