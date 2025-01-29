package com.hotelbooking.controllers;

import com.hotelbooking.models.Customer;
import com.hotelbooking.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerController {
    @FXML private TextField nameField;
    @FXML private TextField contactInfoField;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> idColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> contactInfoColumn;

    private DatabaseService databaseService = new DatabaseService();

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactInfoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));

        // Load customers into the table
        customerTable.getItems().addAll(databaseService.getAllCustomers());
    }

    @FXML
    public void handleAddCustomer() {
        String name = nameField.getText();
        String contactInfo = contactInfoField.getText();

        if (!name.isEmpty() && !contactInfo.isEmpty()) {
            boolean success = databaseService.addCustomer(name, contactInfo);
            if (success) {
                showAlert("Success", "Customer added successfully!");
                customerTable.getItems().clear();
                customerTable.getItems().addAll(databaseService.getAllCustomers());
                nameField.clear();
                contactInfoField.clear();
            } else {
                showAlert("Error", "Failed to add customer.");
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