package com.paymentservice.payment.exceptions;

import java.util.List;

public class BankServerException extends RuntimeException {
    private final List<Object> errors;

    public BankServerException(List<Object> errors) {
        super();
        this.errors = errors;
    }

    public List<Object> getErrors() {
        return errors;
    }
}
