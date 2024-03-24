-- liquibase formatted sql

-- changeset catorleader:16
ALTER TABLE tg_chat
RENAME COLUMN dialogstate TO dialog_state;
-- rollback ALTER TABLE tg_chat RENAME COLUMN dialog_state TO dialogstate;

