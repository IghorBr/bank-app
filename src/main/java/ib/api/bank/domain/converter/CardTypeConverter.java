package ib.api.bank.domain.converter;

import ib.api.bank.domain.model.enumerators.CardType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardTypeConverter implements AttributeConverter<CardType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CardType cardType) {
        return cardType.getCode();
    }

    @Override
    public CardType convertToEntityAttribute(Integer integer) {
        return CardType.fromCode(integer);
    }
}
