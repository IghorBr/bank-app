package ib.api.bank.core.security.authorization;

import ib.api.bank.core.security.token.TokenService;
import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final TokenService tokenService;
    private final AccountService accountService;

    public Optional<Account> authorize(String jwt) {
        var id = this.tokenService.load(jwt);
        return this.accountService.findById(id);
    }

}
