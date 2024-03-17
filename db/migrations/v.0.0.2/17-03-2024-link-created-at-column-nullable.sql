-- liquibase formatted sql

-- changeset catorleader:10 dbms:postgresql
ALTER TABLE link
ALTER created_at
DROP NOT NULL;
-- rollback ALTER TABLE link ALTER created_at SET NOT NULL;
