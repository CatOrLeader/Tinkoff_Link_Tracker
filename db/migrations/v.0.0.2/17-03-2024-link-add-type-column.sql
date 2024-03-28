-- liquibase formatted sql

-- changeset catorleader:8
ALTER TABLE link
ADD COLUMN type VARCHAR(16);
-- rollback ALTER TABLE link DROP COLUMN type;
