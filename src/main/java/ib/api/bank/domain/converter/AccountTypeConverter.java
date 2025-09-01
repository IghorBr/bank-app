package ib.api.bank.domain.converter;

import ib.api.bank.domain.model.enumerators.AccountType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountTypeConverter implements AttributeConverter<AccountType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AccountType type) {
        return type.getCode();
    }

    @Override
    public AccountType convertToEntityAttribute(Integer integer) {
        return AccountType.fromCode(integer);
    }
}
