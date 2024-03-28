-- liquibase formatted sql

-- changeset catorleader:5
ALTER TABLE link
ADD COLUMN last_checked_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();
-- rollback ALTER TABLE link DROP COLUMN last_checked_at;
