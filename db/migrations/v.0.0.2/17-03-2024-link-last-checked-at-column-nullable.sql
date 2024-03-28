-- liquibase formatted sql

-- changeset catorleader:12
ALTER TABLE link
ALTER last_checked_at
DROP NOT NULL;
-- rollback ALTER TABLE link ALTER last_checked_at SET NOT NULL;
