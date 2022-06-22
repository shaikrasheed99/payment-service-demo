package com.paymentservice.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentservice.response.ErrorResponse;
import com.paymentservice.payment.exceptions.BankServerException;
import com.paymentservice.payment.exceptions.InvalidRequestBodyException;
import com.paymentservice.payment.exceptions.PaymentsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class PaymentExceptionHandler {

    @Autowired
    private ErrorResponse errorResponse;

    @ExceptionHandler(value = {
            InvalidRequestBodyException.class
    })
    public ResponseEntity<?> handleInvalidRequestBodyException(InvalidRequestBodyException exception) throws JsonProcessingException {
        for (Object error : exception.getErrors()) {
            errorResponse.addError(error);
        }
        String response = errorResponse.convertToJson();
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {
            BankServerException.class
    })
    public ResponseEntity<?> handleBankServerException(BankServerException exception) throws JsonProcessingException {
        for (Object error : exception.getErrors()) {
            errorResponse.addError(error);
        }
        String response = errorResponse.convertToJson();
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {
            PaymentsNotFoundException.class,
    })
    public ResponseEntity<?> handleUserNotFoundException(PaymentsNotFoundException exception) throws JsonProcessingException {
        for (Object error : exception.getErrors()) {
            errorResponse.addError(error);
        }
        String response = errorResponse.convertToJson();
        return ResponseEntity.status(NOT_FOUND).body(response);
    }
}
