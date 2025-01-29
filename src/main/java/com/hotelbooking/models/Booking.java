package com.hotelbooking.models;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int roomId;
    private int customerId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    // Constructor, Getters, and Setters
    public Booking(int id, int roomId, int customerId, LocalDate checkInDate, LocalDate checkOutDate) {
        this.id = id;
        this.roomId = roomId;
        this.customerId = customerId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public int getCustomerId() { return customerId; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
}