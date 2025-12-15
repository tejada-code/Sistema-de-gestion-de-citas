# Database Schema Fix Instructions

## Problem
The application is failing to start because the `veterinario` table in your database doesn't have the required `id` column.

## Solution Options

### Option 1: Execute the SQL Script (Recommended)

1. **Connect to your MySQL database** using any MySQL client (MySQL Workbench, phpMyAdmin, or command line)

2. **Execute the SQL script** `fix_veterinario_table.sql`:
   ```sql
   -- Fix veterinario table structure
   DROP TABLE IF EXISTS `veterinario`;
   
   CREATE TABLE `veterinario` (
       `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
       `numero_colegio_medico` VARCHAR(255),
       `especialidad` VARCHAR(255),
       `horario_laboral` VARCHAR(255),
       `id_usuario` BIGINT NOT NULL,
       FOREIGN KEY (`id_usuario`) REFERENCES `usuario`(`id`)
   );
   ```

### Option 2: Let Hibernate Create the Table (Quick Fix)

1. **Temporarily change DDL mode** in `application.properties`:
   ```properties
   spring.jpa.hibernate.ddl-auto=create-drop
   ```

2. **Start the application** - Hibernate will create all tables from scratch

3. **Change back to validate** after first run:
   ```properties
   spring.jpa.hibernate.ddl-auto=validate
   ```

### Option 3: Manual Database Commands

If you want to preserve existing data, run these commands in your database:

```sql
-- Add the missing id column
ALTER TABLE `veterinario` ADD COLUMN `id` BIGINT AUTO_INCREMENT PRIMARY KEY FIRST;

-- Add other missing columns
ALTER TABLE `veterinario` ADD COLUMN `numero_colegio_medico` VARCHAR(255);
ALTER TABLE `veterinario` ADD COLUMN `especialidad` VARCHAR(255);
ALTER TABLE `veterinario` ADD COLUMN `horario_laboral` VARCHAR(255);
ALTER TABLE `veterinario` ADD COLUMN `id_usuario` BIGINT NOT NULL;

-- Add foreign key constraint
ALTER TABLE `veterinario` ADD CONSTRAINT `fk_veterinario_usuario` 
FOREIGN KEY (`id_usuario`) REFERENCES `usuario`(`id`);
```

## Database Connection Details

Your database connection is:
- **Host**: hopper.proxy.rlwy.net:20781
- **Database**: railway
- **Username**: root
- **Password**: nAwdXbDXbAHAlxSVVigcDnDQgAYXlnno

## After Fixing

1. **Restart your Spring Boot application**
2. **Test the endpoints**:
   - `GET http://localhost:8081/api/Veterinario`
   - `GET http://localhost:8081/api/Mascota`
3. **Check the application logs** for any remaining errors

## Expected Result

After fixing the database schema, both endpoints should return:
- **200 OK** with data (if records exist)
- **204 No Content** (if no records exist)
- **No more 500 errors** 