package ib.api.bank.domain.repository;

import ib.api.bank.domain.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);
}
