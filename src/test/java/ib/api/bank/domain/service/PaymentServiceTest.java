package ib.api.bank.domain.service;

import ib.api.bank.domain.exception.BankException;
import ib.api.bank.domain.exception.NotFoundObjectException;
import ib.api.bank.domain.model.*;
import ib.api.bank.domain.model.enumerators.CardType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentServiceTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private PaymentService paymentService;

    private Account firstAccount;

    @BeforeAll
    void setupInit() {
        User user = new User("John", "john@email.com", "123");
        Account account = new Account("123", BigDecimal.valueOf(1000));

        this.firstAccount = this.accountService.createAccount(account, user);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(this.firstAccount, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void mustCreatePayment1Portion() {
        this.firstAccount = this.accountService.newCreditCard(this.firstAccount);
        Card creditCard = this.firstAccount.getCards().stream().filter(c -> c.getType() == CardType.CREDIT && c.getActive()).findFirst().orElseThrow(() -> new NotFoundObjectException("Credit card not found"));
        String description = "Payment description";

        Payment payment = this.paymentService.createPayment(creditCard, BigDecimal.valueOf(50), description, 1);

        Assertions.assertEquals(description, payment.getDescription());
        Assertions.assertEquals(1, payment.getPortions().size());
    }

    @Test
    void mustNotCreatePaymentWithInvalidCard() {
        Card debitCard = this.firstAccount.getCards().stream().filter(c -> c.getType() == CardType.DEBIT && c.getActive()).findFirst().orElseThrow(() -> new NotFoundObjectException("Credit card not found"));
        BigDecimal value = BigDecimal.valueOf(50);
        String description = "Payment description";

        Assertions.assertThrows(BankException.class, () ->
            this.paymentService.createPayment(debitCard, value, description, 2)
        );
    }

    @Test
    void mustCreatePaymentMultiplePortions() {
        this.firstAccount = this.accountService.newCreditCard(this.firstAccount);
        Card creditCard = this.firstAccount.getCards().stream().filter(c -> c.getType() == CardType.CREDIT && c.getActive()).findFirst().orElseThrow(() -> new NotFoundObjectException("Credit card not found"));
        String description = "Payment description";
        int numberOfPortions = 3;
        BigDecimal amount = BigDecimal.valueOf(127.99);

        Payment payment = this.paymentService.createPayment(creditCard, amount, description, numberOfPortions);
        BigDecimal totalPortions = payment.getPortions().stream().map(Portion::getAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        Assertions.assertEquals(description, payment.getDescription());
        Assertions.assertEquals(numberOfPortions, payment.getPortions().size());
        Assertions.assertEquals(amount, totalPortions);
    }

    @Test
    void mustCreateDebitPayment() {
        Card debitCard = this.firstAccount.getCards().stream().filter(c -> c.getType() == CardType.DEBIT && c.getActive()).findFirst().orElseThrow(() -> new NotFoundObjectException("Credit card not found"));
        BigDecimal value = BigDecimal.valueOf(50);
        String description = "Payment description";

        Payment payment = this.paymentService.createPayment(debitCard, value, description, 1);
        Assertions.assertEquals(description, payment.getDescription());
        Assertions.assertEquals(1, payment.getPortions().size());
        Assertions.assertEquals(BigDecimal.valueOf(950), this.firstAccount.getBalance());
    }
}
