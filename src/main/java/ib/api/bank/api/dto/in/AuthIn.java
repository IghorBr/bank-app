package ib.api.bank.api.dto.in;

import jakarta.validation.constraints.NotBlank;

public record AuthIn(@NotBlank String accountNumber, @NotBlank String password) {
}
