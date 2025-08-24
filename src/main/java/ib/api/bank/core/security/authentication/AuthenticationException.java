package ib.api.bank.core.security.authentication;

import ib.api.bank.domain.exception.BankException;

public class AuthenticationException extends BankException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
