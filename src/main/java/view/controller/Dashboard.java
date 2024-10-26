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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dashboard extends Application {

    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private PieChart booksPieChart;

    @FXML
    private Label changeAccountButton;

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

        changeAccountButton.setOnMouseClicked(event -> {
            try {
                Parent loginParent = FXMLLoader.load(getClass().getResource("/fxml/LoginUI.fxml"));
                Scene loginScene = new Scene(loginParent);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(loginScene);
                window.centerOnScreen();
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Set up the PieChart
        PieChart.Data borrowedBooks = new PieChart.Data("Borrowed Books", 5);
        PieChart.Data remainingBooks = new PieChart.Data("Remaining Books", 15);

        booksPieChart.getData().addAll(borrowedBooks, remainingBooks);
    }

    public static void main(String[] args) {
        launch(args);
    }
}