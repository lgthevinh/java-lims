package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

import static com.controller.MainController.loopApplication;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        loopApplication(stage);
    }
}