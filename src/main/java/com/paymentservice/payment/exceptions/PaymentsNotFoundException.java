package com.paymentservice.payment.exceptions;

import java.util.List;

public class PaymentsNotFoundException extends RuntimeException {
    private final List<Object> errors;

    public PaymentsNotFoundException(List<Object> errors) {
        this.errors = errors;
    }

    public List<Object> getErrors() {
        return errors;
    }
}
