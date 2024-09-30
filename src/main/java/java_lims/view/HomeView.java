package java_lims.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HomeView {
    public static void showHomeView(Stage window) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(HomeView.class.getResource("/fxml/HomeScene.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);

        window.setTitle("Java LiMS - Library Management System");
        window.setScene(scene);

        window.show();
    }
}
