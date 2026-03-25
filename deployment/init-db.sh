#!/bin/bash
set -e

# 1) crear las bases y los privilegios
for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE $db;
    GRANT ALL PRIVILEGES ON DATABASE $db TO $POSTGRES_USER;
EOSQL
done

# 2) crear tablas e insertar datos

## 2.1) ms1db: Tariff
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname ms1db <<-EOSQL
CREATE TABLE IF NOT EXISTS Tariff (
  id SERIAL PRIMARY KEY,
  laps           INTEGER NOT NULL,
  max_minutes    INTEGER NOT NULL,
  price          BIGINT NOT NULL,
  total_duration INTEGER NOT NULL
);
INSERT INTO Tariff (laps, max_minutes, price, total_duration) VALUES
  (10, 10, 15000, 30),
  (15, 15, 20000, 35),
  (20, 20, 25000, 40)
ON CONFLICT DO NOTHING;
EOSQL

## 2.2) ms2db: group_size_dsc
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname ms2db <<-EOSQL
CREATE TABLE IF NOT EXISTS group_size_dsc (
  id_group_size_dsc   SERIAL PRIMARY KEY,
  min_group_size      INTEGER NOT NULL,
  max_group_size      INTEGER NOT NULL,
  discount_percentage NUMERIC(5,2) NOT NULL
);
INSERT INTO group_size_dsc (min_group_size, max_group_size, discount_percentage) VALUES
  (1,  2,  0.0),
  (3,  5, 10.0),
  (6, 10, 20.0),
  (11,15, 30.0)
ON CONFLICT DO NOTHING;
EOSQL

## 2.3) ms3db: frequency_dsc
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname ms3db <<-EOSQL
CREATE TABLE IF NOT EXISTS frequency_dsc (
  id_frequency_dsc    SERIAL PRIMARY KEY,
  frequency_type      VARCHAR(50) NOT NULL,
  min_frequency       INTEGER NOT NULL,
  max_frequency       INTEGER NOT NULL,
  discount_percentage NUMERIC(5,2) NOT NULL
);
INSERT INTO frequency_dsc (frequency_type, min_frequency, max_frequency, discount_percentage) VALUES
  ('Muy frecuente',   7, 1000000, 30.0),
  ('Frecuente',       5,       6, 20.0),
  ('Regular',         2,       4, 10.0),
  ('No frecuente',    0,       1,  0.0)
ON CONFLICT DO NOTHING;
EOSQL

## 2.4) ms4db: SpecialDay
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname ms4db <<-EOSQL
CREATE TABLE IF NOT EXISTS special_day (
  id_special_day    SERIAL PRIMARY KEY,
  name            VARCHAR(100) NOT NULL,
  date            DATE           NOT NULL,
  special_discount DOUBLE PRECISION NOT NULL
);

INSERT INTO special_day (name, date, special_discount) VALUES
  -- ========== AÑO 2025 ==========
  ('Año Nuevo', '2025-01-01', 1.3),
  ('Viernes Santo', '2025-04-18', 1.4),
  ('Sábado Santo', '2025-04-19', 1.2),
  ('Día del Trabajo', '2025-05-01', 1.3),
  ('Día de las Glorias Navales', '2025-05-21', 1.2),
  ('Día del Padre', '2025-06-15', 0.5),
  ('San Pedro y San Pablo', '2025-06-29', 1.0),
  ('Día de la Virgen del Carmen', '2025-07-16', 1.1),
  ('Fiestas Patrias', '2025-09-18', 1.5),
  ('Día de las Glorias del Ejército', '2025-09-19', 1.4),
  ('Encuentro de Dos Mundos', '2025-10-12', 1.0),
  ('Día de las Iglesias Evangélicas', '2025-10-31', 1.0),
  ('Día de Todos los Santos', '2025-11-01', 1.2),
  ('Inmaculada Concepción', '2025-12-08', 1.1),
  ('Navidad', '2025-12-25', 1.5),
  ('Día de San Valentín', '2025-02-14', 0.8),
  ('Día de la Madre', '2025-05-10', 0.7),

  -- ========== AÑO 2026 ==========
  ('Año Nuevo', '2026-01-01', 1.3),
  ('Viernes Santo', '2026-04-03', 1.4),
  ('Sábado Santo', '2026-04-04', 1.2),
  ('Día del Trabajo', '2026-05-01', 1.3),
  ('Día de las Glorias Navales', '2026-05-21', 1.2),
  ('Día del Padre', '2026-06-21', 0.5),
  ('San Pedro y San Pablo', '2026-06-29', 1.0),
  ('Día de la Virgen del Carmen', '2026-07-16', 1.1),
  ('Fiestas Patrias', '2026-09-18', 1.5),
  ('Día de las Glorias del Ejército', '2026-09-19', 1.4),
  ('Encuentro de Dos Mundos', '2026-10-12', 1.0),
  ('Día de las Iglesias Evangélicas', '2026-10-31', 1.0),
  ('Día de Todos los Santos', '2026-11-01', 1.2),
  ('Inmaculada Concepción', '2026-12-08', 1.1),
  ('Navidad', '2026-12-25', 1.5),
  ('Aniversario Local', '2026-08-15', 0.6),

  -- ========== AÑO 2027 ==========
  ('Año Nuevo', '2027-01-01', 1.3),
  ('Viernes Santo', '2027-03-26', 1.4),
  ('Sábado Santo', '2027-03-27', 1.2),
  ('Día del Trabajo', '2027-05-01', 1.3),
  ('Día de las Glorias Navales', '2027-05-21', 1.2),
  ('Día del Padre', '2027-06-20', 0.5),
  ('San Pedro y San Pablo', '2027-06-29', 1.0),
  ('Día de la Virgen del Carmen', '2027-07-16', 1.1),
  ('Fiestas Patrias', '2027-09-18', 1.5),
  ('Día de las Glorias del Ejército', '2027-09-19', 1.4),
  ('Encuentro de Dos Mundos', '2027-10-12', 1.0),
  ('Día de las Iglesias Evangélicas', '2027-10-31', 1.0),
  ('Día de Todos los Santos', '2027-11-01', 1.2),
  ('Inmaculada Concepción', '2027-12-08', 1.1),
  ('Navidad', '2027-12-25', 1.5),

  -- ========== AÑO 2028 ==========
  ('Año Nuevo', '2028-01-01', 1.3),
  ('Viernes Santo', '2028-04-14', 1.4),
  ('Sábado Santo', '2028-04-15', 1.2),
  ('Día del Trabajo', '2028-05-01', 1.3),
  ('Día de las Glorias Navales', '2028-05-21', 1.2),
  ('Día del Padre', '2028-06-18', 0.5),
  ('San Pedro y San Pablo', '2028-06-29', 1.0),
  ('Día de la Virgen del Carmen', '2028-07-16', 1.1),
  ('Fiestas Patrias', '2028-09-18', 1.5),
  ('Día de las Glorias del Ejército', '2028-09-19', 1.4),
  ('Encuentro de Dos Mundos', '2028-10-12', 1.0),
  ('Día de las Iglesias Evangélicas', '2028-10-31', 1.0),
  ('Día de Todos los Santos', '2028-11-01', 1.2),
  ('Inmaculada Concepción', '2028-12-08', 1.1),
  ('Navidad', '2028-12-25', 1.5)
ON CONFLICT DO NOTHING;
EOSQL

# Si necesitas datos base para ms5db–ms7db, añade aquí más bloques:
# ## 2.5) ms5db: … 
# psql … --dbname ms5db <<-EOSQL
# INSERT INTO … ON CONFLICT DO NOTHING;
# EOSQL