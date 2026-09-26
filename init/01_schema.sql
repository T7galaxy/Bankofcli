
CREATE TABLE accounts (
                          account_id    VARCHAR(50) PRIMARY KEY,
                          pin_hash      VARCHAR(255) NOT NULL,
                          balance       NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
                          created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT chk_balance_non_negative CHECK (balance >= 0)
);


CREATE TABLE transactions (
                              transaction_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              account_id          VARCHAR(50) NOT NULL REFERENCES accounts(account_id),
                              related_account_id  VARCHAR(50) REFERENCES accounts(account_id),
                              type                VARCHAR(20) NOT NULL CHECK (type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER')),
                              amount              NUMERIC(15, 2) NOT NULL CHECK (amount > 0),
                              created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE record_logs (
                             log_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             record_id       VARCHAR(50) NOT NULL,
                             field_changed   VARCHAR(100) NOT NULL,
                             old_value       TEXT,
                             new_value       TEXT,
                             changed_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_record_logs_record_id ON record_logs(record_id);