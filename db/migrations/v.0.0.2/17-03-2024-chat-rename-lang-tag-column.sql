-- liquibase formatted sql

-- changeset catorleader:9
ALTER TABLE tg_chat
RENAME COLUMN languagetag TO language_tag;
-- rollback ALTER TABLE tg_chat RENAME COLUMN language_tag TO languagetag;

