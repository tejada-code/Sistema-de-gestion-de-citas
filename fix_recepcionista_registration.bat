@echo off
echo ========================================
echo Fix para registro de recepcionistas
echo ========================================
echo.

echo Ejecutando corrección de la base de datos...
mysql -h hopper.proxy.rlwy.net -P 20781 -u root -pnAwdXbDXbAHAlxSVVigcDnDQgAYXlnno railway < simple_clinica_fix.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ ¡Fix aplicado exitosamente!
    echo.
    echo Ahora puedes registrar recepcionistas con @recepcionista.com
    echo sin problemas.
) else (
    echo.
    echo ❌ Error al aplicar el fix.
    echo Verifica la conexión a la base de datos.
)

echo.
pause 