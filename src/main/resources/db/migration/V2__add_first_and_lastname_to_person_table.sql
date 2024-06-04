ALTER TABLE person
    ADD COLUMN firstname VARCHAR,
    ADD COLUMN lastname VARCHAR;

UPDATE person p
SET
    firstname = split_part(p.name, ' ', 1),
    lastname = split_part(p.name, ' ', 2)
FROM person po
WHERE p.name = po.name;

ALTER TABLE person
    ALTER COLUMN firstname SET NOT NULL,
    ALTER COLUMN lastname SET NOT NULL,
    DROP COLUMN name;
