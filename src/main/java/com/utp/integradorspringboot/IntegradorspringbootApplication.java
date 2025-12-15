package com.utp.integradorspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class IntegradorspringbootApplication implements CommandLineRunner {


    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(IntegradorspringbootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Database Connection Test ===");
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ Database connection successful!");
            System.out.println("Database: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("URL: " + connection.getMetaData().getURL());
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== End Database Test ===");
    }
}
