CREATE SEQUENCE users_id_seq;

CREATE TABLE users (
    id            BIGINT       NOT NULL DEFAULT nextval('users_id_seq') PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    phone         VARCHAR(20),
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ
);