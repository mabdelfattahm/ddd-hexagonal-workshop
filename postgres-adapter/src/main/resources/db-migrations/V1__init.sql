CREATE TABLE IF NOT EXISTS demo.accounts (
 account_id VARCHAR(80) NOT NULL PRIMARY KEY,
 start_balance DOUBLE PRECISION NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS demo.activities (
 activity_id SERIAL PRIMARY KEY,
 source_account VARCHAR(80) REFERENCES accounts(account_id),
 target_account VARCHAR(80) REFERENCES accounts(account_id),
 time_stamp TIMESTAMP NOT NULL,
 money DOUBLE PRECISION NOT NULL
);