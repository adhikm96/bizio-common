package com.thebizio.commonmodule.exception;

public class InvalidAddressException extends Throwable{
    public InvalidAddressException(Exception exception) {
        super(exception);
    }

    public InvalidAddressException(String msg) {
        super(msg);
    }
}
