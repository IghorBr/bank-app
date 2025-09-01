package ib.api.bank.domain.model;

import ib.api.bank.domain.exception.BankException;
import ib.api.bank.domain.model.enumerators.AccountType;
import ib.api.bank.domain.util.BigDecimalUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "TB_ACCOUNT")
@Getter @Setter
@NoArgsConstructor
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private String password;

    private AccountType accountType;

    @Setter(AccessLevel.NONE)
    private BigDecimal balance;

    private Boolean active = Boolean.TRUE;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<Card> cards = new ArrayList<>();

    public Account(String password, BigDecimal balance) {
        this.password = password;
        this.balance = balance;
        this.accountType = AccountType.getTypeFromBalance(this.balance);
    }

    public Account(String accountNumber, String password, BigDecimal balance, User user) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
        this.accountType = AccountType.getTypeFromBalance(this.balance);
        this.user = user;
    }

    public void deposit(BigDecimal amount) {
        if (Boolean.FALSE.equals(this.active))
            throw new BankException("Account is inactive");

        if (BigDecimalUtils.isNegative(amount)) {
            throw new BankException("Amount must be greater than zero");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (Boolean.FALSE.equals(this.active))
            throw new BankException("Account is inactive");

        if (BigDecimalUtils.isNegative(amount)) {
            throw new BankException("Amount must be greater than zero");
        }
        this.balance = this.balance.subtract(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;
        return Objects.equals(getId(), account.getId()) && Objects.equals(getAccountNumber(), account.getAccountNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAccountNumber());
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
