package ib.api.bank.domain.converter;

import ib.api.bank.domain.util.EncryptUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SecurityConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String s) {
        return EncryptUtils.encryptToBase64(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return EncryptUtils.decryptFromBase64(s);
    }
}
