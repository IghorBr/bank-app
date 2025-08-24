package ib.api.bank.core.exception_handler;

import ib.api.bank.domain.exception.BankException;
import ib.api.bank.domain.exception.NotFoundObjectException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private HttpStatusCode notFound = HttpStatusCode.valueOf(404);
    private HttpStatusCode badRequest = HttpStatusCode.valueOf(400);
    private HttpStatusCode internalServerError = HttpStatusCode.valueOf(500);

    @ExceptionHandler(BankException.class)
    public ResponseEntity<Object> handleBankException(BankException ex, WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String path = request.getRequestURI();

        var error = new Error(path, ex.getMessage(), this.badRequest.value(), java.time.LocalDateTime.now());
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), this.badRequest, webRequest);
    }

    @ExceptionHandler(NotFoundObjectException.class)
    public ResponseEntity<Object> handleNotFoundObjectException(NotFoundObjectException ex, WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String path = request.getRequestURI();

        var error = new Error(path, ex.getMessage(), this.notFound.value(), java.time.LocalDateTime.now());
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), this.notFound, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
}
