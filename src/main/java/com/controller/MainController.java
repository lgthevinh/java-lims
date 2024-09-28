package com.controller;

import javafx.stage.Stage;

import static com.view.HomeView.showHomeView;

public class MainController {

    // Initializing logic stage goes in here
    public static void initializeApplication() {

    }

    // Main loop of application goes in here
    public static void loopApplication(Stage stage) {
        showHomeView(stage);
    }
}
