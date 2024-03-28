-- liquibase formatted sql

-- changeset catorleader:15
ALTER TABLE link
ALTER last_checked_at
TYPE TIMESTAMP WITHOUT TIME ZONE;
-- rollback ALTER TABLE link ALTER last_checked_at TYPE TIMESTAMP WITH TIME ZONE;
