package com.hotelbooking.controllers;

import com.hotelbooking.models.User;
import com.hotelbooking.services.UserService;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Label errorLabel;

    private final UserService userService;

    public LoginController() {
        this.userService = new UserService();
    }

    @FXML
    public void initialize() {
        // Clear any previous error messages
        errorLabel.setText("");
        
        // Enable login button only when both fields have content
        loginButton.disableProperty().bind(
            usernameField.textProperty().isEmpty()
            .or(passwordField.textProperty().isEmpty())
        );

        // Add enter key handler for password field
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleLogin();
            }
        });
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        try {
            userService.authenticate(username, password)
                .ifPresentOrElse(
                    this::loginSuccess,
                    this::loginFailed
                );
        } catch (SQLException e) {
            showError("Database error occurred. Please try again later.");
            e.printStackTrace();
        }
    }

    private void loginSuccess(User user) {
        try {
            // Store user in session
            SessionManager.getInstance().setCurrentUser(user);

            // Load dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotelbooking/views/dashboard.fxml"));
            AnchorPane dashboard = loader.load();

            // Get current stage
            Stage stage = (Stage) loginButton.getScene().getWindow();
            
            // Set new scene
            Scene scene = new Scene(dashboard);
            stage.setScene(scene);
            stage.setTitle("Hotel Booking System - Dashboard");
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            showError("Error loading dashboard. Please try again.");
            e.printStackTrace();
        }
    }

    private void loginFailed() {
        showError("Invalid username or password");
        passwordField.clear();
        usernameField.requestFocus();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}