package ib.api.bank.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_PAYMENT")
@Getter @Setter
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private Boolean paid;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Portion> portions = new HashSet<>();

    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.paid = false;
        this.active = true;
    }

    public Payment(String description, BigDecimal amount, Card card) {
        this();
        this.description = description;
        this.amount = amount;
        this.card = card;
    }
}
