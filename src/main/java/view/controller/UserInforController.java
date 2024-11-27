package view.controller;

import animatefx.animation.ZoomIn;
import com.lims.dao.DatabaseManager;
import com.lims.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.ZoneId;

public class UserInforController {
    @FXML
    private TextField socialIdField;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private TextField addressLineField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    private User loggedInUser;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;

        socialIdField.setText(user.getSocialId());
        nameField.setText(user.getName());
        addressLineField.setText(user.getAddressLine());
        phoneNumberField.setText(user.getPhoneNumber());
        emailField.setText(user.getEmail());
        passwordField.setText("");
        if (user.getDateOfBirth() != null) {
            dateOfBirthField.setValue(user.getDateOfBirth().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        }

    }

    @FXML
    private void handleUpdateUser() {
        if (passwordField.getText().equals(loggedInUser.getPassword())) {
            loggedInUser.setSocialId(socialIdField.getText());
            loggedInUser.setName(nameField.getText());
            loggedInUser.setAddressLine(addressLineField.getText());
            loggedInUser.setPhoneNumber(phoneNumberField.getText());
            loggedInUser.setEmail(emailField.getText());
            if (dateOfBirthField.getValue() != null) {
                loggedInUser.setDateOfBirth(java.sql.Date.valueOf(dateOfBirthField.getValue()));
            }
            try {
                DatabaseManager.updateUserInDatabase(loggedInUser);
                showAlert("Success", "Information updated successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update information.");
            }
        } else {
            showAlert("Error", "Incorrect password.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainUserView.fxml"));
            ZoomIn zoomIn = new ZoomIn(root);
            zoomIn.setSpeed(1.0);
            zoomIn.play();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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
