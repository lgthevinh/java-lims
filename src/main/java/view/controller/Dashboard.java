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