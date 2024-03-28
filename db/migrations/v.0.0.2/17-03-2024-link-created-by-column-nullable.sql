-- liquibase formatted sql

-- changeset catorleader:11
ALTER TABLE link
ALTER created_by
DROP NOT NULL;
-- rollback ALTER TABLE link ALTER created_by SET NOT NULL;
