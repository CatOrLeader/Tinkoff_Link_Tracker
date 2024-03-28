--liquibase formatted sql

--changeset catorleader:3
CREATE TABLE chat_link_assignment(
    chat_id VARCHAR(20) REFERENCES tg_chat (id),
    link_id BIGINT REFERENCES link (id)
);
--rollback DROP TABLE chat_link_assignment;
