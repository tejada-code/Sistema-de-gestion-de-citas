-- Drop tables in reverse dependency order to avoid FK issues
DROP TABLE IF EXISTS boleta_pago;
DROP TABLE IF EXISTS detalle_cita;
DROP TABLE IF EXISTS notificacion;
DROP TABLE IF EXISTS historial_clinico;
DROP TABLE IF EXISTS cita;
DROP TABLE IF EXISTS mascota;
DROP TABLE IF EXISTS recepcionista;
DROP TABLE IF EXISTS empleado_clinica;
DROP TABLE IF EXISTS veterinario;
DROP TABLE IF EXISTS dueno;
DROP TABLE IF EXISTS sesion;
DROP TABLE IF EXISTS usuario_rol;
DROP TABLE IF EXISTS rol;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS clinica_veterinaria;

-- 1. Usuario
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(255),
    apellidos VARCHAR(255),
    dni VARCHAR(20),
    celular VARCHAR(20),
    fecha_nacimiento DATE,
    fecha_registro DATETIME
);

-- 2. Rol
CREATE TABLE rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255)
);

-- 3. Usuario_rol
CREATE TABLE usuario_rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_rol BIGINT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_rol) REFERENCES rol(id)
);

-- 4. Veterinario
CREATE TABLE veterinario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    numero_colegio_medico VARCHAR(255),
    especialidad VARCHAR(255),
    horario_laboral VARCHAR(255),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- 5. Clinica_veterinaria
CREATE TABLE clinica_veterinaria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_clinica VARCHAR(255),
    ruc VARCHAR(20),
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    link_web VARCHAR(255),
    id_veterinario BIGINT NOT NULL,
    plan_suscripcion VARCHAR(255),
    pasarela_pago VARCHAR(255),
    FOREIGN KEY (id_veterinario) REFERENCES veterinario(id)
);

-- 6. Dueno
CREATE TABLE dueno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- 7. Mascota
CREATE TABLE mascota (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    especie VARCHAR(255),
    raza VARCHAR(255),
    edad INT,
    id_dueno BIGINT NOT NULL,
    id_clinica BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,
    FOREIGN KEY (id_dueno) REFERENCES dueno(id),
    FOREIGN KEY (id_clinica) REFERENCES clinica_veterinaria(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- 8. Recepcionista
CREATE TABLE recepcionista (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    horario_laboral VARCHAR(255),
    id_clinica BIGINT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_clinica) REFERENCES clinica_veterinaria(id)
);

-- 9. Empleado_clinica
CREATE TABLE empleado_clinica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT,
    id_clinica BIGINT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_clinica) REFERENCES clinica_veterinaria(id)
);

-- 10. Cita
CREATE TABLE cita (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    hora TIME,
    estado VARCHAR(255),
    id_mascota BIGINT,
    id_veterinario BIGINT NOT NULL,
    id_dueno BIGINT NOT NULL,
    FOREIGN KEY (id_mascota) REFERENCES mascota(id),
    FOREIGN KEY (id_veterinario) REFERENCES veterinario(id),
    FOREIGN KEY (id_dueno) REFERENCES dueno(id)
);

-- 11. Motivo_cita (NEW TABLE)
CREATE TABLE motivo_cita (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    activo BOOLEAN DEFAULT TRUE
);

-- 12. Detalle_cita
CREATE TABLE detalle_cita (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cita BIGINT NOT NULL,
    id_motivo_cita BIGINT,
    estado VARCHAR(255),
    motivo_consulta VARCHAR(255),
    diagnostico VARCHAR(255),
    tratamiento VARCHAR(255),
    receta VARCHAR(255),
    costo DECIMAL(10,2),
    metodo_pago VARCHAR(255),
    duracion_aproximada INT,
    FOREIGN KEY (id_cita) REFERENCES cita(id),
    FOREIGN KEY (id_motivo_cita) REFERENCES motivo_cita(id)
);

-- 13. Boleta_pago (NEW TABLE)
CREATE TABLE boleta_pago (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_detalle_cita BIGINT NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(255) NOT NULL,
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_detalle_cita) REFERENCES detalle_cita(id),
    UNIQUE KEY unique_detalle_cita (id_detalle_cita)
);

-- 14. Notificacion
CREATE TABLE notificacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(255),
    mensaje VARCHAR(255),
    fecha_creacion DATETIME,
    estado VARCHAR(255),
    id_cita BIGINT NOT NULL,
    FOREIGN KEY (id_cita) REFERENCES cita(id)
);

-- 15. Historial_clinico
CREATE TABLE historial_clinico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    peso DOUBLE,
    diagnostico VARCHAR(255),
    tratamiento VARCHAR(255),
    observaciones VARCHAR(255),
    id_cita BIGINT NOT NULL,
    id_mascota BIGINT NOT NULL,
    FOREIGN KEY (id_cita) REFERENCES cita(id),
    FOREIGN KEY (id_mascota) REFERENCES mascota(id)
);

-- 16. Sesion
CREATE TABLE sesion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    correo VARCHAR(255),
    contrasena VARCHAR(255),
    id_usuario BIGINT NOT NULL,
    fecha_creacion DATETIME,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- Table for password reset tokens
CREATE TABLE IF NOT EXISTS password_reset_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL,
    expiration_date DATETIME NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES usuario(id)
);

-- Insert sample data for testing
INSERT INTO rol (nombre) VALUES 
('ADMINISTRADOR'),
('VETERINARIO'),
('RECEPCIONISTA'),
('DUENO');

-- Insert sample motivo_cita data
INSERT INTO motivo_cita (nombre, descripcion, precio) VALUES 
('Consulta General', 'Consulta veterinaria general', 50.00),
('Vacunación', 'Aplicación de vacunas', 80.00),
('Cirugía', 'Procedimiento quirúrgico', 200.00),
('Examen de Laboratorio', 'Análisis de sangre y otros', 120.00),
('Radiografía', 'Examen radiológico', 150.00),
('Esterilización', 'Procedimiento de esterilización', 180.00);

-- Create indexes for better performance
CREATE INDEX idx_cita_fecha ON cita(fecha);
CREATE INDEX idx_cita_estado ON cita(estado);
CREATE INDEX idx_detalle_cita_estado ON detalle_cita(estado);
CREATE INDEX idx_boleta_pago_fecha ON boleta_pago(fecha_emision);
CREATE INDEX idx_usuario_dni ON usuario(dni); 