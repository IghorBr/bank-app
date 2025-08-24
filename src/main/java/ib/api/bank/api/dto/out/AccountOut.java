package ib.api.bank.api.dto.out;

import java.math.BigDecimal;

public record AccountOut(String accountNumber,
                         String accountType,
                         BigDecimal balance,
                         Boolean active,
                         UserOut userOut) {
}
