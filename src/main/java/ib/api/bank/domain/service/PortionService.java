package ib.api.bank.domain.service;

import ib.api.bank.domain.model.Payment;
import ib.api.bank.domain.model.Portion;

import java.math.BigDecimal;
import java.util.Set;

public interface PortionService {

    Portion save(Portion portion);
    Set<Portion> createPortions(Payment payment, Integer numberOfPortions, BigDecimal amount);
}
