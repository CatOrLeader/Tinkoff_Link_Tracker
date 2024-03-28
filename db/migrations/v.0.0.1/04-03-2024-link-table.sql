--liquibase formatted sql

--changeset catorleader:2
CREATE TABLE IF NOT EXISTS link(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uri VARCHAR(256) UNIQUE NOT NULL,
    description VARCHAR(128),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(64) NOT NULL,
    updated_by VARCHAR(64),
    update_description VARCHAR(128)
);
--rollback DROP TABLE link;
