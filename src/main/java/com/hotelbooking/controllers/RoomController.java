package com.hotelbooking.controllers;

import com.hotelbooking.models.Room;
import com.hotelbooking.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class RoomController {
    @FXML private TextField roomTypeField;
    @FXML private TextField priceField;
    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> idColumn;
    @FXML private TableColumn<Room, String> roomTypeColumn;
    @FXML private TableColumn<Room, Double> priceColumn;
    @FXML private TableColumn<Room, Boolean> availabilityColumn;

    private DatabaseService databaseService = new DatabaseService();

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));

        // Load rooms into the table
        roomTable.getItems().addAll(databaseService.getAllRooms());
    }

    @FXML
    public void handleAddRoom() {
        String roomType = roomTypeField.getText();
        double price = Double.parseDouble(priceField.getText());

        if (!roomType.isEmpty() && price > 0) {
            boolean success = databaseService.addRoom(roomType, price);
            if (success) {
                showAlert("Success", "Room added successfully!");
                roomTable.getItems().clear();
                roomTable.getItems().addAll(databaseService.getAllRooms());
                roomTypeField.clear();
                priceField.clear();
            } else {
                showAlert("Error", "Failed to add room.");
            }
        } else {
            showAlert("Invalid Input", "Please fill all fields correctly.");
        }
    }

    @FXML
    public void handleUpdateAvailability() {
        Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            boolean newAvailability = !selectedRoom.isAvailability();
            boolean success = databaseService.updateRoomAvailability(selectedRoom.getId(), newAvailability);
            if (success) {
                showAlert("Success", "Room availability updated successfully!");
                roomTable.getItems().clear();
                roomTable.getItems().addAll(databaseService.getAllRooms());
            } else {
                showAlert("Error", "Failed to update room availability.");
            }
        } else {
            showAlert("No Selection", "Please select a room to update.");
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