package com.thebizio.commonmodule.exception;

import com.taxjar.exception.TaxjarException;

public class TaxSubmissionException extends Throwable {
    public TaxSubmissionException(TaxjarException e) {
        super(e);
    }

    public TaxSubmissionException(String msg) {
        super(msg);
    }
}
