package ib.api.bank.domain.service.impl;

import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.Card;
import ib.api.bank.domain.model.enumerators.CardType;
import ib.api.bank.domain.repository.CardRepository;
import ib.api.bank.domain.service.CardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public Card save(Card card) {
        return this.cardRepository.save(card);
    }

    @Override
    @Transactional
    public Card createDebitCard(Account account) {
        return this.save(new Card(account.getUser().getName(), CardType.DEBIT, account));
    }

    @Override
    @Transactional
    public Card createCreditCard(Account account) {
        return this.save(new Card(account.getUser().getName(), CardType.CREDIT, account));
    }

    @Override
    public Optional<Card> findById(Long id) {
        return this.cardRepository.findById(id);
    }

    @Override
    public Optional<Card> findByCardNumber(String cardNumber) {
        return this.cardRepository.findByCardNumber(cardNumber);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.findById(id).ifPresent(c -> c.setActive(false));
    }
}
