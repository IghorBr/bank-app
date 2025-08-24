package ib.api.bank.api.mapper;

import ib.api.bank.api.dto.in.AccountIn;
import ib.api.bank.api.dto.out.AccountOut;
import ib.api.bank.api.dto.out.UserOut;
import ib.api.bank.domain.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    private final UserMapper userMapper;

    public Function<Account, AccountOut> toOut() {
        return account -> {
            UserOut userOut = this.userMapper.toOut().apply(account.getUser());

            return new AccountOut(
                    account.getAccountNumber(),
                    account.getAccountType().getDescription(),
                    account.getBalance(),
                    account.getActive(),
                    userOut
            );
        };
    }

    public Function<AccountIn, Account> toDomain() {
        return accountIn -> new Account(
                accountIn.password(),
                accountIn.balance()
        );
    }
}
