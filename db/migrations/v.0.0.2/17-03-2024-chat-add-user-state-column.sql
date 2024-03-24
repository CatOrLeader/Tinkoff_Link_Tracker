-- liquibase formatted sql

-- changeset catorleader:6
ALTER TABLE tg_chat
ADD COLUMN dialogState VARCHAR(32) NOT NULL DEFAULT 'UNINITIALIZED';
-- rollback ALTER TABLE tg_chat DROP COLUMN dialogState;

