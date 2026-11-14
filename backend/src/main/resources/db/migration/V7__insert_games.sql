INSERT INTO games (name, slug)
VALUES
    ('Counter-Strike 2', 'cs2'),
    ('League of Legends', 'lol')
    ON CONFLICT DO NOTHING;