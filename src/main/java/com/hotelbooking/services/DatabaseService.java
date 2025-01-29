package com.hotelbooking.services;

import com.hotelbooking.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private Connection connection;

    public DatabaseService() {
        try {
            String url = "db.url=jdbc:postgresql://ep-square-scene-a8xyb20x-pooler.eastus2.azure.neon.tech/neondb?user=neondb_owner&password=npg_BtdFx6goLRk1&sslmode=require
;
            String user = "collinsmunene";
            String password = "npg_BtdFx6goLRk1&ssl";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new customer
    public boolean addCustomer(String name, String contactInfo) {
        String query = "INSERT INTO Customers (Name, ContactInfo) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, contactInfo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("ContactInfo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    package com.hotelbooking.services;

import com.hotelbooking.models.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private Connection connection;

    public DatabaseService() {
        try {
            String url = "jdbc:postgresql://your-neon-endpoint:5432/your-database";
            String user = "your-username";
            String password = "your-password";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new room
    public boolean addRoom(String roomType, double price) {
        String query = "INSERT INTO Rooms (RoomType, Price, Availability) VALUES (?, ?, TRUE)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, roomType);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all rooms
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM Rooms";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("ID"),
                        rs.getString("RoomType"),
                        rs.getDouble("Price"),
                        rs.getBoolean("Availability")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
   
}