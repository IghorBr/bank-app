package ib.api.bank.api.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UserIn(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @CPF String document
) {
}
