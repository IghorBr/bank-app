package ib.api.bank.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccountIn(
        @NotNull UserIn userIn,
        @NotBlank @Size(min = 3, max = 20) String password,
        @NotNull @Positive BigDecimal balance) {
}
