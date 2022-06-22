package com.paymentservice.user.exceptions;

import java.util.List;

public class UserNotFoundException extends RuntimeException {
    private final List<Object> errors;

    public UserNotFoundException(List<Object> errors) {
        super();
        this.errors = errors;
    }

    public List<Object> getErrors() {
        return errors;
    }
}
