package ib.api.bank.domain.model;

import ib.api.bank.domain.model.enumerators.CardType;
import ib.api.bank.domain.util.Utils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "TB_CARD")
@Getter @Setter
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;
    private String cardHolderName;
    private LocalDate expirationDate;
    private String cvv;
    private Boolean active;

    private BigDecimal maxLimit;
    private BigDecimal availableLimit;

    private CardType type;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public Card() {
        this.cardNumber = Utils.generateCardNumber();
        this.cvv = Utils.generateCVV();
        this.active = Boolean.TRUE;
        this.expirationDate = LocalDate.now().plusYears(5);
    }

    public Card(String cardHolderName, CardType type, Account account) {
        this();
        this.cardHolderName = cardHolderName;
        this.type = type;
        this.account = account;
        this.maxLimit = null;
        this.availableLimit = null;

        if (this.type == CardType.CREDIT) {
            this.maxLimit = this.account.getAccountType().getMaxLimit().divide(BigDecimal.valueOf(2), RoundingMode.HALF_EVEN);
            this.availableLimit = this.maxLimit;
        }
    }
}
