package view.controller;

import animatefx.animation.FadeIn;
import animatefx.animation.ZoomIn;
import com.lims.dao.DatabaseManager;
import com.lims.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpController {
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

    @FXML
    private void handleSignUp(ActionEvent event) {
        String socialId = socialIdField.getText();
        String name = nameField.getText();
        Date dateOfBirth = null;
        if (dateOfBirthField.getValue() != null) {
            dateOfBirth = java.sql.Date.valueOf(dateOfBirthField.getValue());
        }
        String addressLine = addressLineField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        User newUser = new User(socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);

        try {
            DatabaseManager.addUserToDatabase(newUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserInforView.fxml"));
            Parent root = loader.load();
            FadeIn fadeIn = new FadeIn(root);
            fadeIn.setSpeed(0.5);
            fadeIn.play();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}