package com.hotelbooking.controllers;

import com.hotelbooking.Main;

public class DashboardController {
    @FXML
    public void handleGoToBookingView() {
        Main.loadView("/com/hotelbooking/views/booking.fxml", "Hotel Booking System - Booking");
    }

    @FXML
    public void handleGoToCustomerView() {
        Main.loadView("/com/hotelbooking/views/customer.fxml", "Hotel Booking System - Customer Management");
    }

    @FXML
    public void handleGoToRoomView() {
        Main.loadView("/com/hotelbooking/views/room.fxml", "Hotel Booking System - Room Management");
    }
}