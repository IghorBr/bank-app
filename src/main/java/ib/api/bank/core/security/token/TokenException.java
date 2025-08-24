package ib.api.bank.core.security.token;

import ib.api.bank.domain.exception.BankException;

public class TokenException extends BankException {
    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
