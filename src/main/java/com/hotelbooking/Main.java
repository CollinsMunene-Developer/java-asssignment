package com.hotelbooking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            Main.primaryStage = primaryStage;
            primaryStage.setResizable(false);

            // Load the login view as the initial view
            loadView("/com/hotelbooking/views/login.fxml", "Hotel Booking System - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a view (FXML file) into the primary stage.
     *
     * @param fxmlPath The path to the FXML file.
     * @param title    The title of the window.
     */
    public static void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("/styles/main.css").toExternalForm());

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the primary stage (main window) of the application.
     *
     * @return The primary stage.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}