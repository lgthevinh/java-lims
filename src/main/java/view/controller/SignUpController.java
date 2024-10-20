package view.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class SignUpController {

    public void handleSignInAction(ActionEvent event) {
        try {
            // Load the login UI
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/LoginUI.fxml"));
            Scene loginScene = new Scene(loginRoot);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the login scene
            stage.setScene(loginScene);
            stage.centerOnScreen();

            // Create a fade transition
            FadeTransition fadeTransition = new FadeTransition(javafx.util.Duration.millis(1000), loginRoot);
            fadeTransition.setFromValue(0.8);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}