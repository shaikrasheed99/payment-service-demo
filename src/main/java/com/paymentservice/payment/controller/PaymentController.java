package com.paymentservice.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentservice.response.ErrorResponse;
import com.paymentservice.response.SuccessResponse;
import com.paymentservice.payment.model.Payment;
import com.paymentservice.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    private SuccessResponse successResponse;

    @Autowired
    private ErrorResponse errorResponse;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Payment payment) throws JsonProcessingException {
        Payment addedPayment = paymentService.create(payment);
        successResponse.addData(Collections.singletonMap("transactionId", addedPayment.getTransactionId()));
        String response = successResponse.convertToJson();
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getPaymentsByUserId(@PathVariable UUID userId) throws JsonProcessingException {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        successResponse.addData(payments);
        String response = successResponse.convertToJson();
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/users/{userId}/transactions/{transactionId}")
    public ResponseEntity<?> getPaymentsByUserIdAndTransactionId(@PathVariable UUID userId, @PathVariable UUID transactionId) throws JsonProcessingException {
        List<Payment> payments = paymentService.getPaymentsByUserIdAndTransactionId(userId, transactionId);
        successResponse.addData(payments);
        String response = successResponse.convertToJson();
        return ResponseEntity.status(OK).body(response);
    }
}
