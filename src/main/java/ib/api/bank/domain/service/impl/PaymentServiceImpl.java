package ib.api.bank.domain.service.impl;

import ib.api.bank.domain.exception.BankException;
import ib.api.bank.domain.model.Card;
import ib.api.bank.domain.model.Payment;
import ib.api.bank.domain.model.Portion;
import ib.api.bank.domain.model.enumerators.CardType;
import ib.api.bank.domain.repository.PaymentRepository;
import ib.api.bank.domain.service.AccountService;
import ib.api.bank.domain.service.PaymentService;
import ib.api.bank.domain.service.PortionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final AccountService accountService;
    private final PaymentRepository paymentRepository;
    private final PortionService portionService;

    @Override
    @Transactional
    public Payment save(Payment payment) {
        return this.paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment createPayment(Card card, BigDecimal amount, String description, Integer numberOfPortions) {
        if (numberOfPortions > 1 && card.getType() != CardType.CREDIT) {
            throw new BankException("Only credit cards can have multiple portions");
        }

        if (!Boolean.TRUE.equals(card.getActive())) {
            throw new BankException("Card is inactive");
        }

        Payment payment;

        if (card.getType() == CardType.DEBIT) {
            payment = this.createDebitPayment(card, amount, description);
        } else {
            payment = this.createCreditPayment(card, amount, description, numberOfPortions);
        }

        return this.save(payment);
    }

    private Payment createDebitPayment(Card card, BigDecimal amount, String description) {
        Payment payment = new Payment(description, amount, card);
        Set<Portion> portions = this.portionService.createPortions(payment, 1, amount);
        payment.getPortions().addAll(portions);
        payment.setPaid(true);

        this.accountService.withdraw(amount);

        return payment;
    }

    private Payment createCreditPayment(Card card, BigDecimal amount, String description, Integer numberOfPortions) {
        Payment payment = new Payment(description, amount, card);
        Set<Portion> portions = this.portionService.createPortions(payment, numberOfPortions, amount);
        payment.getPortions().addAll(portions);

        return payment;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return this.paymentRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.findById(id).ifPresent(p -> {
            p.setActive(false);
            p.getPortions().forEach(pt -> pt.setActive(false));

            this.paymentRepository.save(p);
        });
    }
}
