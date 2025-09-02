package ib.api.bank.domain.service;

import ib.api.bank.domain.model.Card;
import ib.api.bank.domain.model.Payment;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentService {

    Payment save(Payment payment);
    Payment createPayment(Card card, BigDecimal amount, String description, Integer portions);

    Optional<Payment> findById(Long id);

    void deleteById(Long id);
}
