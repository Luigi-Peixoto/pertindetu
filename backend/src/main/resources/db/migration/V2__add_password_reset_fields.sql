-- Add columns to store password reset token and its expiry
ALTER TABLE users
    ADD COLUMN reset_token VARCHAR(255);

ALTER TABLE users
    ADD COLUMN reset_token_expiry TIMESTAMP;

