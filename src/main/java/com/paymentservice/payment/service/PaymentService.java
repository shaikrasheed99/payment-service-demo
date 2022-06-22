package com.paymentservice.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentservice.response.BankResponse;
import com.paymentservice.bank.service.BankService;
import com.paymentservice.payment.model.Payment;
import com.paymentservice.payment.exceptions.BankServerException;
import com.paymentservice.payment.exceptions.InvalidRequestBodyException;
import com.paymentservice.payment.exceptions.PaymentsNotFoundException;
import com.paymentservice.payment.model.PaymentRepository;
import com.paymentservice.user.model.User;
import com.paymentservice.user.service.UserService;
import com.paymentservice.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BankService bankService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment create(Payment payment) throws JsonProcessingException {
        List<Object> errors = new ArrayList<>();
        UUID senderId = payment.getSenderId();
        UUID receiverId = payment.getReceiverId();

        if (!isUserIdGiven(senderId)) errors.add(Collections.singletonMap("message", "Sender id should not be empty!"));
        if (!isUserIdGiven(receiverId))
            errors.add(Collections.singletonMap("message", "Receiver id should not be empty!"));
        if (isAmountNegative(payment.getAmount()))
            errors.add(Collections.singletonMap("message", "Amount should not be negative!"));

        if (!errors.isEmpty()) throw new InvalidRequestBodyException(errors);

        User sender = userService.getUserById(senderId);
        if (!isUserExist(sender))
            errors.add(Collections.singletonMap("message", "Sender id is not found in the bank!"));

        User receiver = userService.getUserById(receiverId);
        if (!isUserExist(receiver))
            errors.add(Collections.singletonMap("message", "Receiver id is not found in the bank!"));

        if (!errors.isEmpty()) throw new UserNotFoundException(errors);

        BankResponse response = bankService.makePayment(payment);
        if (response.getStatusCode() >= 400) errors.add(Collections.singletonMap("providerError", response));

        if (!errors.isEmpty()) throw new BankServerException(errors);

        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByUserId(UUID userId) {
        User user = userService.getUserById(userId);
        if (!isUserExist(user))
            throw new UserNotFoundException(List.of(Collections.singletonMap("message", "User is not found with this user id!")));
        List<Payment> payments = paymentRepository.findAllByUserId(userId);
        if (payments.isEmpty())
            throw new PaymentsNotFoundException(List.of(Collections.singletonMap("message", "Payments not found with this transaction id!")));
        return payments;
    }

    public List<Payment> getPaymentsByUserIdAndTransactionId(UUID userId, UUID transactionId) {
        User user = userService.getUserById(userId);
        if (!isUserExist(user))
            throw new UserNotFoundException(List.of(Collections.singletonMap("message", "User is not found with this user id!")));

        List<Payment> payments = paymentRepository.findAllByUserIdAndTransactionId(userId, transactionId);
        if (payments.isEmpty())
            throw new PaymentsNotFoundException(List.of(Collections.singletonMap("message", "Payment is not found with this user id and transaction id!")));
        return payments;
    }

    private boolean isUserExist(User user) {
        return user != null;
    }

    private boolean isAmountNegative(int amount) {
        return amount < 0;
    }

    private boolean isUserIdGiven(UUID userId) {
        return userId != null;
    }
}
