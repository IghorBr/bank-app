CREATE TABLE TB_CARD (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    card_number NVARCHAR(16) NOT NULL,
    card_holder_name NVARCHAR(100) NOT NULL,
    expiration_date DATETIME NOT NULL, -- Formato MM/YY
    cvv NVARCHAR(4) NOT NULL,
    active BIT NOT NULL DEFAULT 1,
    max_limit DECIMAL(19,2) NULL,
    available_limit DECIMAL(19,2) NULL,
    card_type INT NOT NULL, -- Enum mapeado como INT
    account_id BIGINT NOT NULL,
    CONSTRAINT FK_CARDS_ACCOUNTS FOREIGN KEY (account_id)
        REFERENCES TB_ACCOUNT(id)
);