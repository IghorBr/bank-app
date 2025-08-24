package ib.api.bank.core.security.authorization;

import ib.api.bank.domain.exception.BankException;

public class AuthorizationException extends BankException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
