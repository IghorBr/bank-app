package ib.api.bank.core.security.authentication;

import ib.api.bank.core.security.token.TokenService;
import ib.api.bank.domain.exception.NotFoundObjectException;
import ib.api.bank.domain.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenService tokenService;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(String accountNumber, String password) {
        var account = this.accountService.findByAccountNumber(accountNumber).orElseThrow(() -> new NotFoundObjectException("Account not found"));
        if (!this.passwordEncoder.matches(password, account.getPassword()))
            throw new AuthenticationException("Incorrect password");
        return this.tokenService.tokenize(account.getId());
    }

}
