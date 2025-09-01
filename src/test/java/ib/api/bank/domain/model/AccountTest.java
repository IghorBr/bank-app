package ib.api.bank.domain.model;

import ib.api.bank.domain.exception.BankException;
import ib.api.bank.domain.model.enumerators.AccountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void testBasicAccountCreation() {
        User user = new User("John Doe", "john@email.com", "123");
        Account account = new Account("736302", "password123", BigDecimal.valueOf(100), user);

        assertEquals("736302", account.getAccountNumber());
        assertEquals("password123", account.getPassword());
        assertEquals(account.getBalance(), BigDecimal.valueOf(100));
        assertEquals(AccountType.BASIC, account.getAccountType());
    }

    @Test
    void testMidAccountCreation() {
        User user = new User("John Doe", "john@email.com", "123");
        Account account = new Account("736302", "password123", BigDecimal.valueOf(10000), user);

        assertEquals("736302", account.getAccountNumber());
        assertEquals("password123", account.getPassword());
        assertEquals(account.getBalance(), BigDecimal.valueOf(10000));
        assertEquals(AccountType.MID, account.getAccountType());
    }

    @Test
    void testPrimeAccountCreation() {
        User user = new User("John Doe", "john@email.com", "123");
        Account account = new Account("736302", "password123", BigDecimal.valueOf(25000), user);

        assertEquals("736302", account.getAccountNumber());
        assertEquals("password123", account.getPassword());
        assertEquals(account.getBalance(), BigDecimal.valueOf(25000));
        assertEquals(AccountType.PRIME, account.getAccountType());
    }

    @Test
    void testBlackAccountCreation() {
        User user = new User("John Doe", "john@email.com", "123");
        Account account = new Account("736302", "password123", BigDecimal.valueOf(100000), user);

        assertEquals("736302", account.getAccountNumber());
        assertEquals("password123", account.getPassword());
        assertEquals(account.getBalance(), BigDecimal.valueOf(100000));
        assertEquals(AccountType.BLACK, account.getAccountType());
    }

    @Test
    void mustDeposit() {
        User user = new User("John Doe", "john@email.com", "123");
        Account account = new Account("736302", "password123", BigDecimal.valueOf(100), user);

        account.deposit(BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(150), account.getBalance());
    }

    @Test
    void mustNotDepositNegativeAmount() {
        User user = new User("John Doe", "john@email.com", "123");
        Account account = new Account("736302", "password123", BigDecimal.valueOf(100), user);
        BigDecimal value = BigDecimal.valueOf(-50);

        assertThrows(BankException.class, () -> account.deposit(value));
    }

    @Test
    void mustWithDraw() {
        User user = new User("John Doe", "john@email.com", "123");
        Account account = new Account("736302", "password123", BigDecimal.valueOf(100), user);

        account.withdraw(BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());
    }

    @Test
    void mustNotWithdrawNegativeAmount() {
        User user = new User("John Doe", "john@email.com", "123");
        Account account = new Account("736302", "password123", BigDecimal.valueOf(100), user);
        BigDecimal value = BigDecimal.valueOf(-50);

        assertThrows(BankException.class, () -> account.withdraw(value));
    }
}
