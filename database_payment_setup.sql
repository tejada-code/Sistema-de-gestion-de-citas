-- Script de configuración de base de datos para el sistema de gestión de pagos
-- Sistema de Gestión de Clínicas Veterinarias

-- Crear tabla boleta_pago si no existe
CREATE TABLE IF NOT EXISTS `boleta_pago` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_detalle_cita` bigint NOT NULL,
  `monto_total` decimal(10,2) NOT NULL,
  `metodo_pago` varchar(50) NOT NULL,
  `fecha_emision` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_boleta_detalle_cita` (`id_detalle_cita`),
  KEY `idx_boleta_fecha` (`fecha_emision`),
  KEY `idx_boleta_metodo` (`metodo_pago`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla motivo_cita si no existe
CREATE TABLE IF NOT EXISTS `motivo_cita` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `precio_base` decimal(10,2) NOT NULL DEFAULT 0.00,
  `activo` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `idx_motivo_activo` (`activo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar datos de muestra para motivo_cita
INSERT INTO `motivo_cita` (`nombre`, `descripcion`, `precio_base`, `activo`) VALUES
('Consulta General', 'Revisión general del estado de salud de la mascota', 50.00, 1),
('Vacunación', 'Aplicación de vacunas según calendario', 80.00, 1),
('Desparasitación', 'Tratamiento antiparasitario interno y externo', 45.00, 1),
('Esterilización', 'Procedimiento quirúrgico de esterilización', 200.00, 1),
('Cirugía Menor', 'Procedimientos quirúrgicos menores', 150.00, 1),
('Radiografía', 'Estudio radiológico diagnóstico', 120.00, 1),
('Análisis de Sangre', 'Exámenes de laboratorio básicos', 90.00, 1),
('Tratamiento de Heridas', 'Limpieza y tratamiento de heridas', 75.00, 1),
('Control Post-operatorio', 'Seguimiento después de cirugía', 40.00, 1),
('Emergencia', 'Atención de emergencia veterinaria', 100.00, 1)
ON DUPLICATE KEY UPDATE
`nombre` = VALUES(`nombre`),
`descripcion` = VALUES(`descripcion`),
`precio_base` = VALUES(`precio_base`),
`activo` = VALUES(`activo`);

-- Verificar que la tabla detalle_cita existe y tiene la columna id_motivo_cita
-- Si no existe, la creamos
CREATE TABLE IF NOT EXISTS `detalle_cita` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_cita` bigint NOT NULL,
  `id_motivo_cita` bigint NOT NULL,
  `descripcion` text,
  `precio` decimal(10,2) NOT NULL DEFAULT 0.00,
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_detalle_cita` (`id_cita`),
  KEY `idx_detalle_motivo` (`id_motivo_cita`),
  CONSTRAINT `fk_detalle_cita_cita` FOREIGN KEY (`id_cita`) REFERENCES `cita` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detalle_motivo_cita` FOREIGN KEY (`id_motivo_cita`) REFERENCES `motivo_cita` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Agregar columna id_motivo_cita a detalle_cita si no existe
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'detalle_cita' 
   AND COLUMN_NAME = 'id_motivo_cita') = 0,
  'ALTER TABLE detalle_cita ADD COLUMN id_motivo_cita bigint NOT NULL AFTER id_cita',
  'SELECT "Column id_motivo_cita already exists" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Agregar foreign key si no existe
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'detalle_cita' 
   AND COLUMN_NAME = 'id_motivo_cita' 
   AND CONSTRAINT_NAME = 'fk_detalle_motivo_cita') = 0,
  'ALTER TABLE detalle_cita ADD CONSTRAINT fk_detalle_motivo_cita FOREIGN KEY (id_motivo_cita) REFERENCES motivo_cita(id)',
  'SELECT "Foreign key fk_detalle_motivo_cita already exists" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS `idx_boleta_detalle_cita` ON `boleta_pago` (`id_detalle_cita`);
CREATE INDEX IF NOT EXISTS `idx_boleta_fecha_emision` ON `boleta_pago` (`fecha_emision`);
CREATE INDEX IF NOT EXISTS `idx_boleta_metodo_pago` ON `boleta_pago` (`metodo_pago`);
CREATE INDEX IF NOT EXISTS `idx_detalle_cita_motivo` ON `detalle_cita` (`id_motivo_cita`);
CREATE INDEX IF NOT EXISTS `idx_motivo_cita_activo` ON `motivo_cita` (`activo`);

-- Insertar datos de muestra para detalle_cita (si no existen)
INSERT INTO `detalle_cita` (`id_cita`, `id_motivo_cita`, `descripcion`, `precio`) 
SELECT c.id, 1, 'Consulta general programada', 50.00
FROM `cita` c 
WHERE c.id NOT IN (SELECT id_cita FROM detalle_cita)
LIMIT 10
ON DUPLICATE KEY UPDATE
`id_motivo_cita` = VALUES(`id_motivo_cita`),
`descripcion` = VALUES(`descripcion`),
`precio` = VALUES(`precio`);

-- Mostrar información de las tablas creadas
SELECT 'Tablas del sistema de pagos:' as info;
SHOW TABLES LIKE '%boleta%';
SHOW TABLES LIKE '%motivo%';
SHOW TABLES LIKE '%detalle%';

-- Mostrar estructura de las tablas principales
DESCRIBE boleta_pago;
DESCRIBE motivo_cita;
DESCRIBE detalle_cita;

-- Mostrar datos de muestra
SELECT 'Motivos de cita disponibles:' as info;
SELECT * FROM motivo_cita WHERE activo = 1;

SELECT 'Detalles de citas con precios:' as info;
SELECT 
    dc.id,
    dc.id_cita,
    mc.nombre as motivo,
    dc.precio,
    dc.descripcion
FROM detalle_cita dc
JOIN motivo_cita mc ON dc.id_motivo_cita = mc.id
LIMIT 10; 