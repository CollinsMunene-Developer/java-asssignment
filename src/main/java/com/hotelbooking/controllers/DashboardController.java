@FXML
private void handleLogout() {
    SessionManager.getInstance().clearSession();
    
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotelbooking/views/login.fxml"));
        AnchorPane login = loader.load();
        
        Stage stage = (Stage) /* your logout button */.getScene().getWindow();
        Scene scene = new Scene(login);
        stage.setScene(scene);
        stage.setTitle("Hotel Booking System - Login");
        stage.setResizable(false);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}