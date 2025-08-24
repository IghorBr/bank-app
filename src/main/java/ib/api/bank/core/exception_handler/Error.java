package ib.api.bank.core.exception_handler;

import java.time.LocalDateTime;

public record Error(String path, String message, Integer status, LocalDateTime timestamp) {
}
