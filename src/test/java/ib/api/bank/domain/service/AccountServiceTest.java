package ib.api.bank.domain.service;

import ib.api.bank.domain.exception.NotFoundObjectException;
import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.Card;
import ib.api.bank.domain.model.User;
import ib.api.bank.domain.model.enumerators.AccountType;
import ib.api.bank.domain.model.enumerators.CardType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Account firstAccount;
    private Account secondAccount;

    @BeforeEach
    void setupInit() {
        User user = new User("John", "john@email.com", "123");
        Account account = new Account("123", BigDecimal.valueOf(100));

        User user1 = new User("Mary", "mary@email.com", "456");
        Account account1 = new Account("456", BigDecimal.valueOf(100));

        this.firstAccount = this.accountService.createAccount(account, user);
        this.secondAccount = this.accountService.createAccount(account1, user1);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(this.firstAccount, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void setupDestroy() {
        this.accountService.deleteById(this.firstAccount.getId());
        this.accountService.deleteById(this.secondAccount.getId());
    }

    @Test
    void testCreateAccount() {
        Assertions.assertNotNull(this.firstAccount.getId());
        Assertions.assertEquals(AccountType.BASIC, this.firstAccount.getAccountType());
        Assertions.assertNotNull(this.firstAccount.getAccountNumber());
        Assertions.assertEquals(7, this.firstAccount.getAccountNumber().length());
        Assertions.assertTrue(this.passwordEncoder.matches("123", this.firstAccount.getPassword()));
        Assertions.assertFalse(this.firstAccount.getCards().isEmpty());

        Card firstCard = this.firstAccount.getCards().getFirst();
        Assertions.assertEquals(16, firstCard.getCardNumber().length());
        Assertions.assertEquals(CardType.DEBIT, firstCard.getType());
    }

    @Test
    void testDepositAccount() {
        this.firstAccount = this.accountService.deposit(BigDecimal.valueOf(50));

        Assertions.assertNotNull(this.firstAccount.getId());
        Assertions.assertEquals(150., this.firstAccount.getBalance().doubleValue());
    }

    @Test
    void testWithdrawAccount() {
        this.firstAccount = this.accountService.withdraw(BigDecimal.valueOf(50));

        Assertions.assertNotNull(this.firstAccount.getId());
        Assertions.assertEquals(50., this.firstAccount.getBalance().doubleValue());
    }

    @Test
    void testTransferMoney() {
        this.firstAccount = this.accountService.transferMoney(this.secondAccount.getAccountNumber(), BigDecimal.valueOf(50));
        this.secondAccount = this.accountService.findByAccountNumber(this.secondAccount.getAccountNumber()).orElseThrow(() -> new NotFoundObjectException("Destination account not found"));

        Assertions.assertNotNull(this.firstAccount.getId());
        Assertions.assertEquals(50., this.firstAccount.getBalance().doubleValue());
        Assertions.assertEquals(150., this.secondAccount.getBalance().doubleValue());
    }

    @Test
    void testNewCreditCard() {
        this.firstAccount = this.accountService.newCreditCard(this.firstAccount);
        BigDecimal limit = this.firstAccount.getAccountType().getMaxLimit().divide(BigDecimal.valueOf(2), RoundingMode.HALF_EVEN);

        Assertions.assertNotNull(this.firstAccount.getId());
        Assertions.assertEquals(2, this.firstAccount.getCards().size());

        Card secondCard = this.firstAccount.getCards().getLast();
        Assertions.assertEquals(16, secondCard.getCardNumber().length());
        Assertions.assertEquals(CardType.CREDIT, secondCard.getType());
        Assertions.assertEquals(limit, secondCard.getMaxLimit());
    }
}
