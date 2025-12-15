-- Add missing tables for payment management system

-- Create motivo_cita table if it doesn't exist
CREATE TABLE IF NOT EXISTS motivo_cita (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    activo BOOLEAN DEFAULT TRUE
);

-- Create boleta_pago table if it doesn't exist
CREATE TABLE IF NOT EXISTS boleta_pago (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_detalle_cita BIGINT NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(255) NOT NULL,
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_detalle_cita) REFERENCES detalle_cita(id),
    UNIQUE KEY unique_detalle_cita (id_detalle_cita)
);

-- Add id_motivo_cita column to detalle_cita if it doesn't exist
ALTER TABLE detalle_cita ADD COLUMN IF NOT EXISTS id_motivo_cita BIGINT;
ALTER TABLE detalle_cita ADD CONSTRAINT IF NOT EXISTS fk_detalle_motivo FOREIGN KEY (id_motivo_cita) REFERENCES motivo_cita(id);

-- Insert sample motivo_cita data if table is empty
INSERT INTO motivo_cita (nombre, descripcion, precio) 
SELECT * FROM (SELECT 'Consulta General', 'Consulta veterinaria general', 50.00) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM motivo_cita WHERE nombre = 'Consulta General') LIMIT 1;

INSERT INTO motivo_cita (nombre, descripcion, precio) 
SELECT * FROM (SELECT 'Vacunación', 'Aplicación de vacunas', 80.00) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM motivo_cita WHERE nombre = 'Vacunación') LIMIT 1;

INSERT INTO motivo_cita (nombre, descripcion, precio) 
SELECT * FROM (SELECT 'Cirugía', 'Procedimiento quirúrgico', 200.00) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM motivo_cita WHERE nombre = 'Cirugía') LIMIT 1;

INSERT INTO motivo_cita (nombre, descripcion, precio) 
SELECT * FROM (SELECT 'Examen de Laboratorio', 'Análisis de sangre y otros', 120.00) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM motivo_cita WHERE nombre = 'Examen de Laboratorio') LIMIT 1;

INSERT INTO motivo_cita (nombre, descripcion, precio) 
SELECT * FROM (SELECT 'Radiografía', 'Examen radiológico', 150.00) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM motivo_cita WHERE nombre = 'Radiografía') LIMIT 1;

INSERT INTO motivo_cita (nombre, descripcion, precio) 
SELECT * FROM (SELECT 'Esterilización', 'Procedimiento de esterilización', 180.00) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM motivo_cita WHERE nombre = 'Esterilización') LIMIT 1;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_boleta_pago_fecha ON boleta_pago(fecha_emision);
CREATE INDEX IF NOT EXISTS idx_motivo_cita_activo ON motivo_cita(activo); 