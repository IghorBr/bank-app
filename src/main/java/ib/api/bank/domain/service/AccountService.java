package ib.api.bank.domain.service;

import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    Account save(Account account);
    Account createAccount(Account account, User user);
    Account deposit(BigDecimal amount);
    Account withdraw(BigDecimal amount);
    Account transferMoney(String toAccountNumber, BigDecimal amount);
    Account newCreditCard(Account account);

    Optional<Account> findById(Long id);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findAllByUser(Long userId);

    void deleteById(Long id);
}
