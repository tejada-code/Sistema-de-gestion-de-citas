-- Debug script to check database state and fix issues

-- 1. Check if cita table has data
SELECT COUNT(*) as total_citas FROM cita;

-- 2. Check if detalle_cita table has data
SELECT COUNT(*) as total_detalles FROM detalle_cita;

-- 3. Check if motivo_cita table has data
SELECT COUNT(*) as total_motivos FROM motivo_cita;

-- 4. Check if there are citas without detalle_cita
SELECT c.id, c.fecha, c.hora, c.estado, 
       CASE WHEN dc.id IS NULL THEN 'Sin detalle' ELSE 'Con detalle' END as tiene_detalle
FROM cita c
LEFT JOIN detalle_cita dc ON c.id = dc.id_cita
ORDER BY c.id;

-- 5. If there are citas without detalle_cita, we can create placeholder records
-- (This will be commented out - uncomment if needed)
/*
INSERT INTO detalle_cita (id_cita, id_motivo_cita, estado, diagnostico, tratamiento, receta, duracion_aproximada)
SELECT c.id, 1, 'Pendiente', '', '', '', 0
FROM cita c
LEFT JOIN detalle_cita dc ON c.id = dc.id_cita
WHERE dc.id IS NULL;
*/

-- 6. Check if motivo_cita has at least one record
SELECT * FROM motivo_cita LIMIT 5;

-- 7. If motivo_cita is empty, insert some sample data
-- (This will be commented out - uncomment if needed)
/*
INSERT INTO motivo_cita (nombre, descripcion, precio) VALUES 
('Consulta General', 'Consulta veterinaria general', 50.00),
('Vacunación', 'Aplicación de vacunas', 80.00),
('Cirugía', 'Procedimiento quirúrgico', 200.00),
('Emergencia', 'Atención de emergencia', 150.00);
*/ 