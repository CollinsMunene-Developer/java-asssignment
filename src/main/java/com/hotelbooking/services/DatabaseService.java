package com.hotelbooking.services;

import com.hotelbooking.models.Room;
import com.hotelbooking.models.Customer;
import com.hotelbooking.models.Booking;

import java.sql.*;
import java.time.LocalDate;
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

    // Fetch all available rooms
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM Rooms WHERE Availability = TRUE";
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
        return rooms;
    }

    // Create a new booking
    public boolean createBooking(int roomId, int customerId, LocalDate checkInDate, LocalDate checkOutDate) {
        String query = "INSERT INTO Bookings (RoomID, CustomerID, CheckInDate, CheckOutDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, customerId);
            pstmt.setDate(3, Date.valueOf(checkInDate));
            pstmt.setDate(4, Date.valueOf(checkOutDate));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}