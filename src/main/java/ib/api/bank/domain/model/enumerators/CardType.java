package ib.api.bank.domain.model.enumerators;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardType {

    DEBIT("Debit", 0),
    CREDIT("Credit", 1);

    private final String description;
    private final Integer code;

    public static CardType fromCode(Integer code) {
        for (CardType type : CardType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentType code: " + code);
    }
}
