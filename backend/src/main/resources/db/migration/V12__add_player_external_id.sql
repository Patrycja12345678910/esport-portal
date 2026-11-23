ALTER TABLE players
ADD COLUMN external_id VARCHAR(100);

ALTER TABLE players
ADD CONSTRAINT uq_players_external_id UNIQUE (external_id);