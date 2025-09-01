package ib.api.bank.domain.service.impl;

import ib.api.bank.core.security.AccountContext;
import ib.api.bank.domain.exception.NotFoundObjectException;
import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.Card;
import ib.api.bank.domain.model.User;
import ib.api.bank.domain.repository.AccountRepository;
import ib.api.bank.domain.service.AccountService;
import ib.api.bank.domain.service.CardService;
import ib.api.bank.domain.service.UserService;
import ib.api.bank.domain.util.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountContext accountContext;
    private final PasswordEncoder passwordEncoder;
    private final CardService cardService;

    private static final String ACCOUNT_NOT_FOUND_ERROR = "Account not found";

    @Override
    @Transactional
    public Account save(Account account) {
        return this.accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account createAccount(Account account, User user) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));

        user.getAccounts().add(account);
        account.setUser(user);
        account.setAccountNumber(Utils.getAccountNumber());

        account = this.save(account);

        Card card = this.cardService.createDebitCard(account);
        account.getCards().add(card);

        return this.save(account);
    }

    @Override
    @Transactional
    public Account deposit(BigDecimal amount) {
        Account account = this.accountContext.loadByContext().orElseThrow(() -> new NotFoundObjectException(ACCOUNT_NOT_FOUND_ERROR));
        account.deposit(amount);

        return this.save(account);
    }

    @Override
    @Transactional
    public Account withdraw(BigDecimal amount) {
        Account account = this.accountContext.loadByContext().orElseThrow(() -> new NotFoundObjectException(ACCOUNT_NOT_FOUND_ERROR));
        account.withdraw(amount);

        return this.save(account);
    }

    @Override
    @Transactional
    public Account transferMoney(String toAccountNumber, BigDecimal amount) {
        Account destinationAccount = this.accountRepository.findByAccountNumber(toAccountNumber).orElseThrow(() -> new NotFoundObjectException("Destination account not found"));
        Account account = this.accountContext.loadByContext().orElseThrow(() -> new NotFoundObjectException(ACCOUNT_NOT_FOUND_ERROR));

        account.withdraw(amount);
        destinationAccount.deposit(amount);

        this.save(destinationAccount);
        return this.save(account);
    }

    @Override
    @Transactional
    public Account newCreditCard(Account account) {
        Card card = this.cardService.createCreditCard(account);
        account.getCards().add(card);

        return this.save(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return this.findById(id);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return this.accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> findAllByUser(Long userId) {
        User user = this.userService.findById(userId).orElseThrow(() -> new NotFoundObjectException("User not found"));
        return this.accountRepository.findAllByUser(user);
    }

    @Override
    public void deleteById(Long id) {
        this.accountRepository.deleteById(id);
    }
}
