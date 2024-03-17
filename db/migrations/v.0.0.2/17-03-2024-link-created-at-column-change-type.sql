-- liquibase formatted sql

-- changeset catorleader:13 dbms:postgresql
ALTER TABLE link
ALTER created_at
TYPE TIMESTAMP WITHOUT TIME ZONE;
-- rollback ALTER TABLE link ALTER created_at TYPE TIMESTAMP WITH TIME ZONE;
