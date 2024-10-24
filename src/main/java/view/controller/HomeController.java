package view.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeController extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/HomeDashboard.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Anydev J-LiMS");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label changeAccountButton;

    @FXML
    private void handleChangeAccountAction(ActionEvent event) {
        try {
            Parent loginParent = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene loginScene = new Scene(loginParent);

            // Get the stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            window.setScene(loginScene);
            window.centerOnScreen();
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Set up the date and time labels
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(timeFormatter));
            dateLabel.setText(now.format(dateFormatter));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        // Set up the change account button
        changeAccountButton.setOnMouseClicked(event -> {
            try {
                Parent loginParent = FXMLLoader.load(getClass().getResource("/fxml/LoginUI.fxml"));
                Scene loginScene = new Scene(loginParent);

                // Get the stage information
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Add a fade transition
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), loginParent);
                fadeTransition.setFromValue(0.8);
                fadeTransition.setToValue(1.0);
                fadeTransition.play();

                // Set the new scene
                window.setScene(loginScene);
                window.centerOnScreen();
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}