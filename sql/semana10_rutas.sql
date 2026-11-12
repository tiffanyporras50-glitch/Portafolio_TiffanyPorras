/*
  Semana 10 - Ajustes en la base de datos techshop
  Ejecutar en MySQL Workbench (con el usuario root).

  Las tablas usuario, rol, usuario_rol y ruta ya existen: se crearon con
  creaTablas.sql en la semana 3. Este script solo agrega las rutas públicas
  que el proyecto usa y que no venían en ese script.
*/
use techshop;

-- 1) Revisar lo que ya hay
select * from usuario;
select * from rol;
select * from usuario_rol;
select r.id_ruta, r.ruta, r.requiere_rol, ro.rol
  from ruta r left join rol ro on r.id_rol = ro.id_rol
  order by r.requiere_rol, r.ruta;

-- 2) Rutas públicas nuevas (solo se insertan si no existen todavía)
--    /consultas/**     -> pestañas de categorías del inicio (semana 6) y consultas por precio (semana 8)
--    /acceso_denegado  -> página que se muestra cuando no tiene permiso
INSERT INTO ruta (ruta, requiere_rol)
  SELECT '/consultas/**', false FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM ruta WHERE ruta = '/consultas/**');

INSERT INTO ruta (ruta, requiere_rol)
  SELECT '/acceso_denegado', false FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM ruta WHERE ruta = '/acceso_denegado');

-- 3) Verificar
select * from ruta order by requiere_rol, ruta;
