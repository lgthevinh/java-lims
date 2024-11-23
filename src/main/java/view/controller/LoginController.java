package view.controller;

import animatefx.animation.FadeIn;
import animatefx.animation.ZoomIn;
import com.lims.model.User;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public static final String[][] ADMIN_ACCOUNTS = {
            {"chuanhquoc2410@gmail.com", "24102005"},
            {"2", "2"},
            {"3", "3"}
    };

    public static final Map<String, String> userAccounts = new HashMap<>();

    public LoginController() {
        loadUserAccounts();
    }

    private void loadUserAccounts() {
        try {
            File authFile = new File("src/main/resources/auth/data.txt");
            Scanner scanner = new Scanner(authFile);
            Base64.Decoder decoder = Base64.getDecoder();

            while (scanner.hasNextLine()) {
                String encodedLine = scanner.nextLine();
                String decodedLine = new String(decoder.decode(encodedLine));
                String[] credentials = decodedLine.split(":");

                if (credentials.length == 2) {
                    userAccounts.put(credentials[0], credentials[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidLogin(username, password)) {
            String viewPath = getViewPathForCredentials(username, password);
            if (viewPath != null) {
                showAlert(AlertType.INFORMATION, "Login Successful", "Welcome " + username + "!");
                try {
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource(viewPath));

                    ZoomIn zoomIn = new ZoomIn(root);
                    zoomIn.setSpeed(1.0);
                    zoomIn.play();

                    stage.setScene(new Scene(root));
                    stage.centerOnScreen();
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private boolean isValidLogin(String username, String password) {
        for (String[] admin : ADMIN_ACCOUNTS) {
            if (admin[0].equals(username) && admin[1].equals(password)) {
                return true;
            }
        }

        return userAccounts.containsKey(username) && userAccounts.get(username).equals(password);
    }

    @FXML
    public void handleSignUp(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignUpView.fxml"));

            FadeIn fadeIn = new FadeIn(root);
            fadeIn.setSpeed(0.5);
            fadeIn.play();

            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load SignUp view.");
        }
    }

    private String getViewPathForCredentials(String username, String password) {
        for (String[] admin : ADMIN_ACCOUNTS) {
            if (admin[0].equals(username) && admin[1].equals(password)) {
                return "/fxml/MainView.fxml";
            }
        }
        if (userAccounts.containsKey(username) && userAccounts.get(username).equals(password)) {
            return "/fxml/MainUserView.fxml";
        }
        return null;
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}