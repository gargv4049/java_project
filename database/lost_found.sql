-- Run this PostgreSQL setup script with psql after creating lost_found_db:
--   createdb -U postgres lost_found_db
--   psql -U postgres -d lost_found_db -f .\database\lost_found.sql
-- The schema and development seed data are maintained in one canonical file.
\set ON_ERROR_STOP on
BEGIN;
\ir ../src/main/resources/schema.sql
COMMIT;
