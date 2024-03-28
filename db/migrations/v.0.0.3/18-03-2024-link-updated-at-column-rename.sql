-- liquibase formatted sql

-- changeset catorleader:17
ALTER TABLE link
RENAME COLUMN update_description TO title;
-- rollback ALTER TABLE tg_chat RENAME COLUMN title TO update_description;

