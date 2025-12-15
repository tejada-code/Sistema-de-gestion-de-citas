@echo off
echo ========================================
echo Database Fix for Payment System
echo ========================================
echo.
echo This script will add the missing tables that are causing the 500 error.
echo.
echo Please follow these steps:
echo.
echo 1. Open your MySQL client or database management tool
echo 2. Connect to your Railway database: railway
echo 3. Run the contents of add_missing_tables.sql
echo.
echo The script will:
echo - Create the missing boleta_pago table
echo - Create the missing motivo_cita table  
echo - Add foreign key relationships
echo - Insert sample data
echo - Create performance indexes
echo.
echo After running the SQL script:
echo 1. Restart your Spring Boot application
echo 2. The payment management system should work without errors
echo.
echo Press any key to continue...
pause > nul
echo.
echo SQL Script Contents:
echo ========================================
type add_missing_tables.sql
echo ========================================
echo.
echo Copy and paste the above SQL into your database client.
echo.
pause 