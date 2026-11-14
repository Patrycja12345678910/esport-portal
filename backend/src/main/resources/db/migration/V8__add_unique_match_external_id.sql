ALTER TABLE matches
ADD CONSTRAINT uq_matches_external_id UNIQUE (external_id);