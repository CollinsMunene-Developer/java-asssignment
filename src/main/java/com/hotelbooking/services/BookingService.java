package com.hotelbooking.services;

import com.hotelbooking.models.Booking;
import com.hotelbooking.models.Room;
import com.hotelbooking.models.Customer;
import com.hotelbooking.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingService {
    
    // Create new booking
    public Booking createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (room_id, customer_id, check_in_date, check_out_date, " +
                    "total_price, status, special_requests, number_of_guests, user_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, booking.getRoomId());
            pstmt.setLong(2, booking.getCustomerId());
            pstmt.setDate(3, Date.valueOf(booking.getCheckInDate()));
            pstmt.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            pstmt.setBigDecimal(5, booking.getTotalPrice());
            pstmt.setString(6, booking.getStatus());
            pstmt.setString(7, booking.getSpecialRequests());
            pstmt.setInt(8, booking.getNumberOfGuests());
            pstmt.setLong(9, booking.getUserId());
            pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
            
            return booking;
        }
    }

    // Check room availability
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings " +
                    "WHERE room_id = ? AND status != 'CANCELLED' " +
                    "AND ((check_in_date BETWEEN ? AND ?) " +
                    "OR (check_out_date BETWEEN ? AND ?) " +
                    "OR (check_in_date <= ? AND check_out_date >= ?))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, roomId);
            pstmt.setDate(2, Date.valueOf(checkIn));
            pstmt.setDate(3, Date.valueOf(checkOut));
            pstmt.setDate(4, Date.valueOf(checkIn));
            pstmt.setDate(5, Date.valueOf(checkOut));
            pstmt.setDate(6, Date.valueOf(checkIn));
            pstmt.setDate(7, Date.valueOf(checkOut));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return false;
        }
    }

    // Get booking by ID with room and customer details
    public Optional<Booking> getBookingById(Long id) throws SQLException {
        String sql = "SELECT b.*, r.*, c.* FROM bookings b " +
                    "JOIN rooms r ON b.room_id = r.id " +
                    "JOIN customers c ON b.customer_id = c.id " +
                    "WHERE b.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBooking(rs));
                }
            }
        }
        return Optional.empty();
    }

    // Get all bookings for a specific date range
    public List<Booking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT b.*, r.*, c.* FROM bookings b " +
                    "JOIN rooms r ON b.room_id = r.id " +
                    "JOIN customers c ON b.customer_id = c.id " +
                    "WHERE b.check_in_date BETWEEN ? AND ? " +
                    "OR b.check_out_date BETWEEN ? AND ?";
        
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            pstmt.setDate(3, Date.valueOf(startDate));
            pstmt.setDate(4, Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }
        return bookings;
    }

    // Update booking status
    public void updateBookingStatus(Long bookingId, String newStatus) throws SQLException {
        String sql = "UPDATE bookings SET status = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(3, bookingId);
            
            pstmt.executeUpdate();
        }
    }

    // Helper method to map ResultSet to Booking object
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("b.id"));
        booking.setRoomId(rs.getLong("b.room_id"));
        booking.setCustomerId(rs.getLong("b.customer_id"));
        booking.setCheckInDate(rs.getDate("b.check_in_date").toLocalDate());
        booking.setCheckOutDate(rs.getDate("b.check_out_date").toLocalDate());
        booking.setTotalPrice(rs.getBigDecimal("b.total_price"));
        booking.setStatus(rs.getString("b.status"));
        booking.setSpecialRequests(rs.getString("b.special_requests"));
        booking.setNumberOfGuests(rs.getInt("b.number_of_guests"));
        booking.setUserId(rs.getLong("b.user_id"));
        booking.setCreatedAt(rs.getTimestamp("b.created_at").toLocalDateTime());
        booking.setUpdatedAt(rs.getTimestamp("b.updated_at").toLocalDateTime());

        // Map Room details
        Room room = new Room();
        room.setId(rs.getLong("r.id"));
        room.setRoomType(rs.getString("r.room_type"));
        room.setPrice(rs.getBigDecimal("r.price"));
        booking.setRoom(room);

        // Map Customer details
        Customer customer = new Customer();
        customer.setId(rs.getLong("c.id"));
        customer.setName(rs.getString("c.name"));
        customer.setEmail(rs.getString("c.email"));
        customer.setPhone(rs.getString("c.phone"));
        booking.setCustomer(customer);

        return booking;
    }
}