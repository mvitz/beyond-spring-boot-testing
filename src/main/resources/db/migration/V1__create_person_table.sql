CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

INSERT INTO person (name) VALUES
    ('Michael Vitz'),
    ('Test Mensch'),
    ('Nochein Test');
