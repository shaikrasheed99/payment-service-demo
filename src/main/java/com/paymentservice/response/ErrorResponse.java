package com.paymentservice.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorResponse {
    private List<Object> errors;

    public ErrorResponse() {
        errors = new ArrayList<>();
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void addError(Object error) {
        this.errors.add(error);
    }

    public String convertToJson() throws JsonProcessingException {
        String response = new ObjectMapper().writeValueAsString(this);
        errors = new ArrayList<>();
        return response;
    }
}
