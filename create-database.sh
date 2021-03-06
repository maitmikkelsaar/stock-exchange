#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
CREATE DATABASE "stock";
CREATE USER "stock" WITH ENCRYPTED PASSWORD 'stock';
GRANT ALL PRIVILEGES ON DATABASE "stock" TO "stock";
ALTER USER "stock" WITH SUPERUSER;
EOSQL
