package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.controller.SplashController;
import javafx.scene.image.Image;

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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginUI.fxml"));

        primaryStage.setTitle("Anydev J-LiMS");
        // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}