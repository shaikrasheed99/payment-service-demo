package com.paymentservice.payment.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends CrudRepository<Payment, UUID> {

    @Query(value = "SELECT * FROM payments p WHERE p.sender_id = :userId", nativeQuery = true)
    public List<Payment> findAllByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT * FROM payments p WHERE p.sender_id = :userId and p.transaction_id = :transactionId", nativeQuery = true)
    public List<Payment> findAllByUserIdAndTransactionId(@Param("userId") UUID userId, @Param("transactionId") UUID transactionId);
}
