package view.controller;

import com.lims.model.User;
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

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String viewPath = getViewPathForCredentials(username, password);
        if (viewPath != null) {
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome " + username + "!");
            try {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource(viewPath));
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private String getViewPathForCredentials(String username, String password) {
        if ("1".equals(username) && "1".equals(password)) {
            return "/fxml/MainView.fxml";
        } else if ("2".equals(username) && "2".equals(password)) {
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
