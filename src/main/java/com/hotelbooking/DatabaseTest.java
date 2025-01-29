package com.hotelbooking;

import com.hotelbooking.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Database connection successful!");
            
            // Test query
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT 1");
            if (rs.next()) {
                System.out.println("Query executed successfully!");
            }
            
            DatabaseConnection.closeConnection();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}