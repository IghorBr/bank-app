package ib.api.bank.domain.exception;

public class NotFoundObjectException extends BankException {
    public NotFoundObjectException(String message) {
        super(message);
    }

    public NotFoundObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
