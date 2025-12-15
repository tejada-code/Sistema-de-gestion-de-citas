@echo off
echo ========================================
echo Database Setup Script for Veterinary Clinic
echo ========================================
echo.
echo This script will help you set up the database tables.
echo.
echo Please make sure you have access to your MySQL database.
echo.
echo Options:
echo 1. Run the complete database setup script
echo 2. Just create the missing tables (boleta_pago, motivo_cita)
echo 3. Exit
echo.
set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" goto :complete_setup
if "%choice%"=="2" goto :missing_tables
if "%choice%"=="3" goto :exit
echo Invalid choice. Please try again.
goto :end

:complete_setup
echo.
echo Running complete database setup...
echo Please execute the following SQL commands in your database:
echo.
echo 1. Open your MySQL client or database management tool
echo 2. Connect to your database: railway
echo 3. Run the contents of create_schema_mysql.sql
echo.
echo The script will:
echo - Drop existing tables (if any)
echo - Create all necessary tables
echo - Insert sample data
echo - Create indexes for performance
echo.
pause
goto :end

:missing_tables
echo.
echo Creating only missing tables...
echo Please execute these SQL commands in your database:
echo.
echo -- Create motivo_cita table
echo CREATE TABLE motivo_cita (
echo     id BIGINT AUTO_INCREMENT PRIMARY KEY,
echo     nombre VARCHAR(255) NOT NULL,
echo     descripcion TEXT,
echo     precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
echo     activo BOOLEAN DEFAULT TRUE
echo );
echo.
echo -- Create boleta_pago table
echo CREATE TABLE boleta_pago (
echo     id BIGINT AUTO_INCREMENT PRIMARY KEY,
echo     id_detalle_cita BIGINT NOT NULL,
echo     monto_total DECIMAL(10,2) NOT NULL,
echo     metodo_pago VARCHAR(255) NOT NULL,
echo     fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
echo     FOREIGN KEY (id_detalle_cita) REFERENCES detalle_cita(id),
echo     UNIQUE KEY unique_detalle_cita (id_detalle_cita)
echo );
echo.
echo -- Add foreign key to detalle_cita if not exists
echo ALTER TABLE detalle_cita ADD COLUMN IF NOT EXISTS id_motivo_cita BIGINT;
echo ALTER TABLE detalle_cita ADD CONSTRAINT fk_detalle_motivo FOREIGN KEY (id_motivo_cita) REFERENCES motivo_cita(id);
echo.
echo -- Insert sample motivo_cita data
echo INSERT INTO motivo_cita (nombre, descripcion, precio) VALUES 
echo ('Consulta General', 'Consulta veterinaria general', 50.00),
echo ('Vacunación', 'Aplicación de vacunas', 80.00),
echo ('Cirugía', 'Procedimiento quirúrgico', 200.00),
echo ('Examen de Laboratorio', 'Análisis de sangre y otros', 120.00),
echo ('Radiografía', 'Examen radiológico', 150.00),
echo ('Esterilización', 'Procedimiento de esterilización', 180.00);
echo.
pause
goto :end

:exit
echo Exiting...
goto :end

:end
echo.
echo After running the database setup:
echo 1. Restart your Spring Boot application
echo 2. The application should start without hanging
echo 3. You can then access the payment management system
echo.
pause 