-- liquibase formatted sql

-- changeset catorleader:7
ALTER TABLE tg_chat
ADD COLUMN languageTag VARCHAR(4);
-- rollback ALTER TABLE tg_chat DROP COLUMN languageTag;

