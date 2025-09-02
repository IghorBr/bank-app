package ib.api.bank.domain.service.impl;

import ib.api.bank.domain.model.Payment;
import ib.api.bank.domain.model.Portion;
import ib.api.bank.domain.repository.PortionRepository;
import ib.api.bank.domain.service.PortionService;
import ib.api.bank.domain.util.BigDecimalUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PortionServiceImpl implements PortionService {
    private final PortionRepository portionRepository;

    @Override
    @Transactional
    public Portion save(Portion portion) {
        return this.portionRepository.save(portion);
    }

    @Override
    @Transactional
    public Set<Portion> createPortions(Payment payment, Integer numberOfPortions, BigDecimal amount) {
        BigDecimal base = amount.divide(BigDecimal.valueOf(numberOfPortions), RoundingMode.HALF_EVEN);
        BigDecimal totalAmount = base.multiply(BigDecimal.valueOf(numberOfPortions));
        BigDecimal difference = amount.subtract(totalAmount);

        var isZero = BigDecimalUtils.isZeroOrNull(difference);
        var portions = new HashSet<Portion>();

        for (int i = 1; i <= numberOfPortions; i++) {
            var portionValue = base;

            if (!isZero && i == 1) {
                portionValue = base.add(difference);
            }
            Portion portion = new Portion(i, portionValue, payment);
            portion = this.save(portion);
            portions.add(portion);
        }
        return portions;
    }
}
