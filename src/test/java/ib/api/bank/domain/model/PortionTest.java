package ib.api.bank.domain.model;

import ib.api.bank.domain.exception.NotFoundObjectException;
import ib.api.bank.domain.model.enumerators.CardType;
import ib.api.bank.domain.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

@SpringBootTest
@ActiveProfiles("test")
class PortionTest {
    @Autowired
    private AccountService accountService;

    private Card creditCard;
    private Card deditCard;

    @BeforeEach
    void setupInit() {
        User user = new User("John", "john@email.com", "123");
        Account account = new Account("123", BigDecimal.valueOf(100));

        account = this.accountService.createAccount(account, user);
        this.deditCard = account.getCards().stream().filter(c -> c.getType() == CardType.DEBIT && c.getActive()).findFirst().orElseThrow(() -> new NotFoundObjectException("Card not found"));

        account = this.accountService.newCreditCard(account);
        this.creditCard = account.getCards().stream().filter(c -> c.getType() == CardType.CREDIT && c.getActive()).findFirst().orElseThrow(() -> new NotFoundObjectException("Card not found"));
    }

    @Test
    void testPortionDueDateIsCorrect1PortionBeforeDueDate() {
        LocalDate localDate = Portion.calculatePortionDueDate(LocalDateTime.of(LocalDate.of(2025, Month.SEPTEMBER, 2), LocalTime.now()), 10, 1, this.creditCard);
        Assertions.assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 10), localDate);
    }

    @Test
    void testPortionDueDateIsCorrect2PortionBeforeDueDate() {
        LocalDate localDate = Portion.calculatePortionDueDate(LocalDateTime.of(LocalDate.of(2025, Month.SEPTEMBER, 2), LocalTime.now()), 10, 2, this.creditCard);
        Assertions.assertEquals(LocalDate.of(2025, Month.OCTOBER, 10), localDate);
    }

    @Test
    void testPortionDueDateIsCorrect1PortionAfterDueDate() {
        LocalDate localDate = Portion.calculatePortionDueDate(LocalDateTime.of(LocalDate.of(2025, Month.SEPTEMBER, 25), LocalTime.now()), 10, 1, this.creditCard);
        Assertions.assertEquals(LocalDate.of(2025, Month.OCTOBER, 10), localDate);
    }

    @Test
    void testPortionDueDateIsCorrect2PortionAfterDueDate() {
        LocalDate localDate = Portion.calculatePortionDueDate(LocalDateTime.of(LocalDate.of(2025, Month.SEPTEMBER, 25), LocalTime.now()), 10, 2, this.creditCard);
        Assertions.assertEquals(LocalDate.of(2025, Month.NOVEMBER, 10), localDate);
    }

    @Test
    void testPortionDueDateIsCorrectDebitCard() {
        LocalDate localDate = Portion.calculatePortionDueDate(LocalDateTime.of(LocalDate.of(2025, Month.SEPTEMBER, 25), LocalTime.now()), 10, 2, this.deditCard);
        Assertions.assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 25), localDate);
    }
}
