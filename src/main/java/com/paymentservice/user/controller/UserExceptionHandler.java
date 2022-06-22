package com.paymentservice.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentservice.response.ErrorResponse;
import com.paymentservice.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class UserExceptionHandler {

    @Autowired
    private ErrorResponse errorResponse;

    @ExceptionHandler(value = {UserNotFoundException.class,})
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) throws JsonProcessingException {
        for (Object error : exception.getErrors()) {
            errorResponse.addError(error);
        }
        String response = errorResponse.convertToJson();
        return ResponseEntity.status(NOT_FOUND).body(response);
    }
}
