-- liquibase formatted sql

-- changeset catorleader:14 dbms:postgresql
ALTER TABLE link
ALTER updated_at
TYPE TIMESTAMP WITHOUT TIME ZONE;
-- rollback ALTER TABLE link ALTER updated_at TYPE TIMESTAMP WITH TIME ZONE;
