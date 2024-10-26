package view.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.util.Duration;


import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuController {

    @FXML
    private Button catalogButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button booksButton;

    @FXML
    private Button usersButton;


    @FXML
    public void initialize() {
        catalogButton.setOnAction(event -> {
            try {
                Parent catalogParent = FXMLLoader.load(getClass().getResource("/fxml/HomeCatalog.fxml"));
                Scene catalogScene = new Scene(catalogParent);

                // Get the stage information
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(catalogScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        dashboardButton.setOnAction(event -> {
            try {
                Parent dashboardParent = FXMLLoader.load(getClass().getResource("/fxml/HomeDashboard.fxml"));
                Scene dashboardScene = new Scene(dashboardParent);

                // Get the stage information
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(dashboardScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        booksButton.setOnAction(event -> {
            try {
                Parent booksParent = FXMLLoader.load(getClass().getResource("/fxml/HomeBooks.fxml"));
                Scene booksScene = new Scene(booksParent);

                // Get the stage information
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(booksScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        usersButton.setOnAction(event -> {
            try {
                Parent usersParent = FXMLLoader.load(getClass().getResource("/fxml/HomeUsers.fxml"));
                Scene usersScene = new Scene(usersParent);

                // Get the stage information
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(usersScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}