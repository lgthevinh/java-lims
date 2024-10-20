package view.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    @FXML
    private void handleLoginAction(ActionEvent event) throws IOException {
        Parent homeParent = FXMLLoader.load(getClass().getResource("/fxml/HomeDashboard.fxml"));
        Scene homeScene = new Scene(homeParent);

        // Get the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Create a fade transition
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), homeParent);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        window.setScene(homeScene);
        window.centerOnScreen();
        window.show();

        // Play the fade transition
        fadeTransition.play();
    }

    public void handleForgotPasswordAction(ActionEvent event) {
        try {
            // Load the forgot password UI
            Parent forgotPasswordRoot = FXMLLoader.load(getClass().getResource("/fxml/ForgotPassword.fxml"));
            Scene forgotPasswordScene = new Scene(forgotPasswordRoot);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the forgot password scene
            stage.setScene(forgotPasswordScene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}