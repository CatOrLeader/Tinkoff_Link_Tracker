--liquibase formatted sql

--changeset catorleader:1
CREATE TABLE IF NOT EXISTS tg_chat(
    id VARCHAR(20) PRIMARY KEY NOT NULL
);
--rollback DROP TABLE tg_chat;
