CREATE TABLE users (
                       id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       email         VARCHAR(254) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role          VARCHAR(20)  NOT NULL,
                       created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
                       CONSTRAINT ck_users_role CHECK (role IN ('ADOPTER', 'SHELTER', 'ADMIN'))
);

-- Emails are unique regardless of upper/lower case
CREATE UNIQUE INDEX uq_users_email_lower ON users (lower(email));