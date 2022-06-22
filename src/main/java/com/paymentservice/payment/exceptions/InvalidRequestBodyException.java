package com.paymentservice.payment.exceptions;

import java.util.List;

public class InvalidRequestBodyException extends RuntimeException {

    private final List<Object> errors;

    public InvalidRequestBodyException(List<Object> errors) {
        super();
        this.errors = errors;
    }

    public List<Object> getErrors() {
        return errors;
    }
}
