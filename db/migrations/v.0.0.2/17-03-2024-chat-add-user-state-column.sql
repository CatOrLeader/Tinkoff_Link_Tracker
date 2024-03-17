-- liquibase formatted sql

-- changeset catorleader:6 dbms:postgresql
ALTER TABLE tg_chat
ADD COLUMN dialogState VARCHAR(16) NOT NULL DEFAULT 'UNINITIALIZED';
-- rollback ALTER TABLE tg_chat DROP COLUMN dialogState;

