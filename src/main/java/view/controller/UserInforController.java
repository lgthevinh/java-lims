package view.controller;

import animatefx.animation.ZoomIn;
import com.lims.dao.DatabaseManager;
import com.lims.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInforController {
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> socialIdColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, Date> dateOfBirthColumn;
    @FXML
    private TableColumn<User, String> addressLineColumn;
    @FXML
    private TableColumn<User, String> phoneNumberColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TextField socialIdField;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private TextField addressLineField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize the user table with the user list
        userTable.setItems(userList);

        // Set up the columns in the table
        socialIdColumn.setCellValueFactory(new PropertyValueFactory<>("socialId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressLineColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dateOfBirthColumn.setCellFactory(column -> {
            return new TableCell<User, Date>() {
                @Override
                protected void updateItem(Date date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(dateFormat.format(date));
                    }
                }
            };
        });

        try {
            userList.addAll(DatabaseManager.getAllUsers());
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleAddUser() {
        String socialId = socialIdField.getText();
        String name = nameField.getText();
        Date dateOfBirth = null;
        if (dateOfBirthField.getValue() != null) {
            dateOfBirth = java.sql.Date.valueOf(dateOfBirthField.getValue());
        }
        String addressLine = addressLineField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        User newUser = new User(socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);

        try {
            DatabaseManager.addUserToDatabase(newUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        userList.add(newUser);
        clearFields();
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                DatabaseManager.deleteUserFromDatabase(selectedUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            userList.remove(selectedUser);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainUserView.fxml"));
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

    private void clearFields() {
        socialIdField.clear();
        nameField.clear();
        dateOfBirthField.setValue(null);
        addressLineField.clear();
        phoneNumberField.clear();
        emailField.clear();
        passwordField.clear();
    }
}