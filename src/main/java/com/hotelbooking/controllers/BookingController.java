package com.hotelbooking.controllers;

import com.hotelbooking.models.Room;
import com.hotelbooking.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;

import java.time.LocalDate;

public class BookingController {
    @FXML private ComboBox<Room> roomComboBox;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;

    private DatabaseService databaseService = new DatabaseService();

    @FXML
    public void initialize() {
        // Populate room combo box
        roomComboBox.getItems().addAll(databaseService.getAvailableRooms());
    }

    @FXML
    public void handleBookRoom() {
        Room selectedRoom = roomComboBox.getValue();
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        if (selectedRoom != null && checkInDate != null && checkOutDate != null) {
            boolean success = databaseService.createBooking(selectedRoom.getId(), 1, checkInDate, checkOutDate); // Replace 1 with actual customer ID
            if (success) {
                showAlert("Booking Successful", "Room booked successfully!");
            } else {
                showAlert("Booking Failed", "Unable to book the room.");
            }
        } else {
            showAlert("Invalid Input", "Please fill all fields.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}