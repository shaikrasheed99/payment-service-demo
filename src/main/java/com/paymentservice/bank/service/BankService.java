package com.paymentservice.bank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentservice.payment.model.Payment;
import com.paymentservice.response.BankResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Service
public class BankService {
    private final Map<UUID, Integer> map;

    public BankService() {
        this.map = new HashMap<>();
    }

    public int balance(UUID id) {
        if (!map.containsKey(id)) {
            creditBonusAmountTo(id);
            return 0;
        }
        return map.get(id);
    }

    public BankResponse makePayment(Payment payment) throws JsonProcessingException {
        UUID senderId = payment.getSenderId();
        UUID receiverId = payment.getReceiverId();

        registerUsersInsideBank(senderId, receiverId);

        if (isBothIdsSame(senderId, receiverId))
            return new BankResponse(BAD_REQUEST.value(), "Transaction got failed, due to sender id and receiver id are same!");

        if (hasSufficientBalanceInSenderAccount(payment, senderId))
            return new BankResponse(BAD_REQUEST.value(), "Transaction got failed, due to insufficient balance in sender's account");

        debitAmountFrom(senderId, payment.getAmount());
        creditAmountTo(receiverId, payment.getAmount());

        return new BankResponse(OK.value(), "Transaction has done successfully");
    }

    private boolean isBothIdsSame(UUID senderId, UUID receiverId) {
        return senderId.equals(receiverId);
    }

    private void creditAmountTo(UUID userId, int amount) {
        map.put(userId, map.get(userId) + amount);
    }

    private void debitAmountFrom(UUID userId, int amount) {
        map.put(userId, map.get(userId) - amount);
    }

    private void creditBonusAmountTo(UUID id) {
        map.put(id, 10);
    }

    private void registerUsersInsideBank(UUID senderId, UUID receiverId) {
        if (!map.containsKey(senderId)) creditBonusAmountTo(senderId);
        if (!map.containsKey(receiverId)) creditBonusAmountTo(receiverId);
    }

    private boolean hasSufficientBalanceInSenderAccount(Payment payment, UUID senderId) {
        return balance(senderId) < payment.getAmount();
    }
}
