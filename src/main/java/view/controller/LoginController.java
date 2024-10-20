// src/main/java/com/example/LoginController.java
package view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private void handleSignUpAction(ActionEvent event) throws IOException {
        Parent signUpParent = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
        Scene signUpScene = new Scene(signUpParent);

        // Get the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(signUpScene);
        window.show();
    }

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

}