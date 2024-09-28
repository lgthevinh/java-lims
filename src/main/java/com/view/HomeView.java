package com.view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeView {
    public static void showHomeView(Stage window) {
        Group root = new Group();
        Scene scene = new Scene(root);

        window.setTitle("Java LiMS - Library Management System");
        window.setScene(scene);

        window.show();
    }
}
