package view.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController extends Application {

    @FXML
    private Button signInButton;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginUI.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Anydev J-LiMS");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        signInButton.setOnAction(event -> {
            try {
                Parent homeRoot = FXMLLoader.load(getClass().getResource("/fxml/HomeScene.fxml"));
                Scene homeScene = new Scene(homeRoot);
                Stage primaryStage = (Stage) signInButton.getScene().getWindow();
                primaryStage.setScene(homeScene);
                primaryStage.setTitle("Anydev J-LiMS");
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}