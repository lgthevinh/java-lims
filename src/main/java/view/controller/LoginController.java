package view.controller;

import animatefx.animation.FadeIn;
import animatefx.animation.ZoomIn;
import com.lims.dao.DatabaseManager;
import com.lims.dao.LibrarianDAO;
import com.lims.model.Librarian;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public static String loggedInUserEmail;

    public static final String[][] ADMIN_ACCOUNTS = {
            {"1", "1"},
            {"2", "2"},
            {"3", "3"}
    };

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidLogin(username, password)) {
            loggedInUserEmail = username;
            String viewPath = getViewPathForCredentials(username, password);
            if (viewPath != null) {
                showAlert(AlertType.INFORMATION, "Login Successful", "Welcome " + username + "!");
                try {
                    User user = DatabaseManager.getUserByEmail(username);
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
                    Parent root = loader.load();

                    UserInforController userInforController = new UserInforController();
                    if(viewPath.equals("/fxml/MainUserView.fxml")) {
                        MainUserViewController controller = loader.getController();
                    }

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

    static boolean isValidLogin(String username, String password) {
        for (String[] admin : ADMIN_ACCOUNTS) {
            if (admin[0].equals(username) && admin[1].equals(password)) {
                return true;
            }
        }
        try {
            Librarian librarian = LibrarianDAO.getLibrarianByEmail(username);
            if (librarian != null && librarian.getPassword().equals(password)) {
                return true;
            }
            User user = DatabaseManager.getUserByEmail(username);
            if (user != null && user.getPassword().equals(password)) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return false;
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
        try {
            Librarian librarian = LibrarianDAO.getLibrarianByEmail(username);
            if (librarian != null && librarian.getPassword().equals(password)) {
                return "/fxml/MainView.fxml";
            }
            User user = DatabaseManager.getUserByEmail(username);
            if (user != null && user.getPassword().equals(password)) {
                return "/fxml/MainUserView.fxml";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
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