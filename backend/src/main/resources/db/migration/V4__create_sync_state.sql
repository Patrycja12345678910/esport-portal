CREATE TABLE sync_state
(
    id  BIGSERIAL PRIMARY KEY,
    source VARCHAR(100) NOT NULL UNIQUE,
    last_successful_sync TIMESTAMPTZ
);