package ib.api.bank.core.security.authentication;

import ib.api.bank.core.security.AccountContext;
import ib.api.bank.domain.model.Account;
import jakarta.validation.constraints.NotNull;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TenantAuthenticationIdentifier implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    private final AccountContext accountContext;

    public TenantAuthenticationIdentifier(AccountContext accountContext) {
        this.accountContext = accountContext;
    }

    @NotNull
    @Override
    public String resolveCurrentTenantIdentifier() {
        return this.accountContext.loadByContext()
                .map(Account::getId)
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return accountContext.loadByContext().isPresent();
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
