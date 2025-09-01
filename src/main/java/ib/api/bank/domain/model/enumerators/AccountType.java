package ib.api.bank.domain.model.enumerators;

import ib.api.bank.domain.exception.NotFoundObjectException;
import ib.api.bank.domain.util.BigDecimalUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    BASIC("Basic", 0, BigDecimal.valueOf(5000)),
    MID("Mid", 1, BigDecimal.valueOf(15000)),
    PRIME("Prime", 2, BigDecimal.valueOf(50000)),
    BLACK("Black", 3, null);

    private final String description;
    private final Integer code;
    private final BigDecimal maxLimit;

    public static AccountType fromCode(Integer code) {
        for (AccountType type : AccountType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new NotFoundObjectException("Invalid AccountType code: " + code);
    }

    public static AccountType getTypeFromBalance(BigDecimal balance) {
        if (BigDecimalUtils.lessThanOrEqual(balance, BASIC.getMaxLimit())) {
            return BASIC;
        } else if (BigDecimalUtils.lessThanOrEqual(balance, MID.getMaxLimit())) {
            return MID;
        } else if (BigDecimalUtils.lessThanOrEqual(balance, PRIME.getMaxLimit())) {
            return PRIME;
        } else {
            return BLACK;
        }
    }

}
