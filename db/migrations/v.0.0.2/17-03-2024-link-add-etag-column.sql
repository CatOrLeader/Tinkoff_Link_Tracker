-- liquibase formatted sql

-- changeset catorleader:4 dbms:postgresql
ALTER TABLE link
ADD COLUMN etag VARCHAR(256);
-- rollback ALTER TABLE link DROP COLUMN etag;
