package ib.api.bank.domain.service;

import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.Card;

import java.util.Optional;

public interface CardService {

    Card save(Card card);
    Card createDebitCard(Account account);
    Card createCreditCard(Account account);

    Optional<Card> findById(Long id);
    Optional<Card> findByCardNumber(String cardNumber);

    void deleteById(Long id);
}
