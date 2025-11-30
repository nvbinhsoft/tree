-- Add email column to users table for email-based authentication
ALTER TABLE users ADD COLUMN email VARCHAR(255);

-- Set email for existing admin user (using username as email for now)
UPDATE users SET email = username || '@example.com' WHERE email IS NULL;

-- Make email required and unique
ALTER TABLE users ALTER COLUMN email SET NOT NULL;
ALTER TABLE users ADD CONSTRAINT users_email_key UNIQUE (email);
