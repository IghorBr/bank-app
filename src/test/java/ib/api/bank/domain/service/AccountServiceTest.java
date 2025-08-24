package ib.api.bank.domain.service;

import ib.api.bank.domain.exception.NotFoundObjectException;
import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.AccountType;
import ib.api.bank.domain.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
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
}
