package view.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class ForgotPasswordController {

    public void handleForgotPasswordAction(ActionEvent event) {
        try {
            // Load the login UI
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/ForgotPassword.fxml"));
            Scene loginScene = new Scene(loginRoot);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the login scene
            stage.setScene(loginScene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleResetPasswordAction(ActionEvent event) {
        try {
            // Load the login UI
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/ResetPassword.fxml"));
            Scene loginScene = new Scene(loginRoot);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the login scene
            stage.setScene(loginScene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}