package view.controller;

import animatefx.animation.FadeIn;
import animatefx.animation.ZoomIn;
import com.lims.model.User;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

import static view.controller.LoginController.userAccounts;


public class SignUpController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    public void handleSignUp(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Passwords do not match!");
            return;
        }

        if (isAdmin(username)) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "This username is reserved for admin accounts!");
            return;
        }

        if (userAccounts.containsKey(username)) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "This username is already taken!");
            return;
        }

        userAccounts.put(username, password);
        saveUserToFile(username, password);

        showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "Your account has been created!");
        System.out.println("User accounts: " + userAccounts);

        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));

            FadeIn fadeIn = new FadeIn(root);
            fadeIn.setSpeed(0.5);
            fadeIn.play();

            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load Login view.");
        }
    }

    private void saveUserToFile(String username, String password) {
        try {
            String concatenated = username + ":" + password;
            String encoded = Base64.getEncoder().encodeToString(concatenated.getBytes());

            File authFile = new File("src/main/resources/auth/data.txt");
            FileWriter fileWriter = new FileWriter(authFile, true);

            fileWriter.write(encoded + "\n");
            fileWriter.close();

            System.out.println("Account saved to authentication file for user: " + username);
        } catch (IOException e) {
            System.out.println("An error occurred while saving authentication data, please try again later...");
            e.printStackTrace();
        }
    }

    private boolean isAdmin(String username) {
        for (String[] admin : LoginController.ADMIN_ACCOUNTS) {
            if (admin[0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
