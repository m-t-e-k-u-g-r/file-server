CREATE TABLE file (
    id uuid PRIMARY KEY DEFAULT pg_catalog.gen_random_uuid() NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    storage_key uuid DEFAULT pg_catalog.gen_random_uuid() NOT NULL,
    size BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE access_key (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_id UUID NOT NULL,
    key_hash VARCHAR NOT NULL,
    description TEXT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    expires_at TIMESTAMP DEFAULT NOW() + INTERVAL '30 days' NOT NULL,
    revoked_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE log_entry (
    id SERIAL PRIMARY KEY,
    access_key_id UUID DEFAULT NULL,
    file_id UUID DEFAULT NULL,
    matches boolean NOT NULL,
    expired boolean NOT NULL,
    revoked boolean NOT NULL,
    authorized boolean GENERATED ALWAYS AS (
        matches AND
        NOT expired AND
        NOT revoked
    ) STORED,
    received TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE config (
    key TEXT UNIQUE PRIMARY KEY,
    value TEXT NOT NULL
);

CREATE INDEX idx_access_key_file_id
    ON access_key(file_id);
