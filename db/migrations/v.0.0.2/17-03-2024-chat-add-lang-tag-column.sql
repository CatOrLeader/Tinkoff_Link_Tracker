-- liquibase formatted sql

-- changeset catorleader:7 dbms:postgresql
ALTER TABLE tg_chat
ADD COLUMN languageTag VARCHAR(4);
-- rollback ALTER TABLE chat DROP COLUMN languageTag;

