CREATE TABLE team_external_ids (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT NOT NULL,
    source VARCHAR(50) NOT NULL,
    external_id VARCHAR(100) NOT NULL,

    CONSTRAINT fk_team_external_ids_team
                               FOREIGN KEY (team_id) REFERENCES teams(id),

    CONSTRAINT uq_team_external_ids_source
                               UNIQUE(source, external_id)
);

INSERT INTO team_external_ids (team_id, source, external_id)
SELECT id, 'pandascore', external_id
FROM teams
WHERE external_id IS NOT NULL;