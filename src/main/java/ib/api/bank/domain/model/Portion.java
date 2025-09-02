package ib.api.bank.domain.model;

import ib.api.bank.domain.model.enumerators.CardType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @Setter
@Table(name = "TB_PORTION")
public class Portion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    public Portion() {
        this.active = true;
    }

    public Portion(Integer number, BigDecimal amount, Payment payment) {
        this();
        this.payment = payment;
        this.number = number;
        this.amount = amount;
        this.paymentDate = Portion.calculatePortionDueDate(this.payment.getPaymentDate(), this.payment.getCard().getDueDate(), this.number, this.payment.getCard());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Portion portion)) return false;
        return Objects.equals(getId(), portion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // baseDate: the date of the first purchase/payment
    // dueDay: the day of month when the card bill is due (e.g., 10)
    // portionNumber: 1 for the first portion, 2 for the second, etc.
    public static LocalDate calculatePortionDueDate(LocalDateTime baseDate, Integer dueDay, Integer portionNumber, Card card) {
        if (card.getType() == CardType.DEBIT)
            return baseDate.toLocalDate();

        LocalDateTime dueDate = baseDate.withDayOfMonth(dueDay);
        if (baseDate.getDayOfMonth() > dueDay) {
            dueDate = dueDate.plusMonths(1);
        }
        dueDate = dueDate.plusMonths(portionNumber - 1L);
        return dueDate.toLocalDate();
    }
}
