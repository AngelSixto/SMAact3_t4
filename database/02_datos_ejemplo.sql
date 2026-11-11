-- =========================================================
-- SMAact3_t4 - Paso 2: datos de ejemplo
-- Ejecutar DESPUES de haber corrido la aplicacion una vez
-- (para que Hibernate ya haya creado las tablas).
-- =========================================================
USE smaact3_db;

INSERT INTO carreras (clave, nombre) VALUES
  ('ISC',  'Ingenieria en Sistemas Computacionales'),
  ('IIND', 'Ingenieria Industrial'),
  ('IGE',  'Ingenieria en Gestion Empresarial'),
  ('ICIV', 'Ingenieria Civil');

-- carrera_id es la LLAVE FORANEA hacia carreras.id
INSERT INTO alumnos (numero_control, nombre, apellidos, email, semestre, carrera_id) VALUES
  ('21160100', 'Angel', 'Sixto Morales',   'angel.sixto@example.com',  7, 1),
  ('21160101', 'Ana',   'Garcia Lopez',    'ana.garcia@example.com',   7, 1),
  ('22160202', 'Luis',  'Perez Ramirez',   'luis.perez@example.com',   5, 2),
  ('23160303', 'Sofia', 'Martinez Cruz',   'sofia.martinez@example.com', 3, 3);

-- =========================================================
-- Consultas para comprobar la relacion (capturas para el README)
-- =========================================================
SELECT * FROM carreras;
SELECT * FROM alumnos;

-- JOIN: cada alumno con el NOMBRE de su carrera
SELECT a.id, a.numero_control, CONCAT(a.nombre, ' ', a.apellidos) AS alumno,
       c.clave, c.nombre AS carrera
FROM alumnos a
JOIN carreras c ON a.carrera_id = c.id;

-- Ver la llave foranea creada por Hibernate
SHOW CREATE TABLE alumnos;
