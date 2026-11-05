CREATE TABLE games
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE teams
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    acronym  VARCHAR(50),
    country  VARCHAR(100),
    logo_url VARCHAR(500)
);

CREATE TABLE players
(
    id          BIGSERIAL PRIMARY KEY,
    nickname    VARCHAR(100) NOT NULL,
    first_name  VARCHAR(100),
    last_name   VARCHAR(100),
    nationality VARCHAR(100)
);

CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE tournaments
(
    id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    status VARCHAR(50),
    external_id VARCHAR(100),

     FOREIGN KEY (game_id) REFERENCES games(id)
);

CREATE TABLE tournament_stages
(
       id BIGSERIAL PRIMARY KEY,
       tournament_id BIGINT NOT NULL,
       name VARCHAR(255) NOT NULL,
       stage_type VARCHAR(50),

       FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);

CREATE TABLE team_rosters
(
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    joined_at TIMESTAMP,
    left_at TIMESTAMP,

    FOREIGN KEY (team_id) REFERENCES teams(id),
    FOREIGN KEY (player_id) REFERENCES players(id)
);

CREATE TABLE matches
(
    id BIGSERIAL PRIMARY KEY,
    tournament_id BIGINT NOT NULL,
    stage_id BIGINT,
    home_team_id BIGINT NOT NULL,
    away_team_id BIGINT NOT NULL,
    scheduled_at TIMESTAMP,
    status VARCHAR(50),
    home_score INT,
    away_score INT,
    external_id VARCHAR(100),

    FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY (stage_id) REFERENCES tournament_stages(id),
    FOREIGN KEY (home_team_id) REFERENCES teams(id),
    FOREIGN KEY (away_team_id) REFERENCES teams(id)
);

CREATE TABLE match_maps
(
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT NOT NULL,
    map_name VARCHAR(100),
    map_number INT,
    home_score INT,
    away_score INT,

    FOREIGN KEY (match_id) REFERENCES matches(id)
);

CREATE TABLE player_match_statistics
(
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    kills INT,
    deaths INT,
    assists INT,
    rating DECIMAL,

    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (player_id) REFERENCES players(id),
    FOREIGN KEY (team_id) REFERENCES teams(id)
);

CREATE TABLE favorite_teams
(
    user_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,

    PRIMARY KEY (user_id,team_id),

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (team_id) REFERENCES teams(id)
);

CREATE TABLE favorite_tournaments
(
    user_id BIGINT NOT NULL,
    tournament_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, tournament_id),

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);