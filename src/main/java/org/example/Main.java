package org.example;

import com.lims.controller.SplashController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        System.setProperty("javafx.preloader", SplashController.class.getName());
        launch(args);
    }

    private void simulateLoadingTasks() throws InterruptedException {
        Thread.sleep(5000);
    }

    @Override
    public void init() throws Exception {
        simulateLoadingTasks();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/HomeScene.fxml")));

        primaryStage.setTitle("Java Library Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}