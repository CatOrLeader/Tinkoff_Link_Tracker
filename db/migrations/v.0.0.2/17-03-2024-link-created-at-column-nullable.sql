-- liquibase formatted sql

-- changeset catorleader:10
ALTER TABLE link
ALTER created_at
DROP NOT NULL;
-- rollback ALTER TABLE link ALTER created_at SET NOT NULL;
