package view.controller;

import animatefx.animation.ZoomIn;
import com.lims.dao.DatabaseManager;
import com.lims.model.Book;
import com.lims.model.Librarian;
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

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class LibrarianController {
    @FXML
    private TableView<Librarian> librarianTable;
    @FXML
    private TableColumn<Librarian, String> socialIdColumn;
    @FXML
    private TableColumn<Librarian, String> nameColumn;
    @FXML
    private TableColumn<Librarian, Date> dateOfBirthColumn;
    @FXML
    private TableColumn<Librarian, String> addressLineColumn;
    @FXML
    private TableColumn<Librarian, String> phoneNumberColumn;
    @FXML
    private TableColumn<Librarian, String> emailColumn;
    @FXML
    private TableColumn<Librarian, Integer> empIdColumn;
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
    @FXML
    private TextField empIdField;
    private ObservableList<Librarian> librarianList = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        // Initialize the librarian table with the librarian list
        librarianTable.setItems(librarianList);
        // Set up the columns in the table
        socialIdColumn.setCellValueFactory(new PropertyValueFactory<>("socialId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressLineColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        empIdColumn.setCellValueFactory(new PropertyValueFactory<>("empId"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dateOfBirthColumn.setCellFactory(column -> {
            return new TableCell<Librarian, Date>() {
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
            librarianList.addAll(DatabaseManager.getAllLibrarians());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleAddLibrarian() {
        try {
            String socialId = socialIdField.getText();
            String empId = null;

            User user = DatabaseManager.getUserBySocialId(socialId);

            if (user == null) {
                System.out.println("User not found!");
                return;
            }

            if (!showAdminLoginDialog()) {
                return;
            }

            DatabaseManager.addLibrarianToDatabase(user);

            Librarian librarian = new Librarian(user, empId);
            librarianList.add(librarian);

            clearFields();
            refreshTable();
        } catch (SQLException e) {
            System.out.println("An error occurred while accessing the database.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid social ID.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean showAdminLoginDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Admin Authentication");
        dialog.setHeaderText("Admin Login Required");
        dialog.setContentText("Please enter your admin username and password (format: username,password):");

        String credentials = dialog.showAndWait().orElse(null);
        if (credentials != null && !credentials.isEmpty()) {
            String[] parts = credentials.split(",");
            if (parts.length == 2) {
                String username = parts[0].trim();
                String password = parts[1].trim();
                return LoginController.isValidLogin(username, password);
            }
        }
        showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid admin credentials.");
        return false;
    }


    @FXML
    private void refreshTable() {
        librarianList.clear();
        try {
            librarianList.addAll(DatabaseManager.getAllLibrarians());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleDeleteLibrarian() {
        Librarian selectedLibrarian = librarianTable.getSelectionModel().getSelectedItem();

        if (selectedLibrarian == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Librarian Selected");
            alert.setContentText("Please select a librarian to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Are you sure you want to delete this librarian?");
        confirmAlert.setContentText("Name: " + selectedLibrarian.getName() + "\nThis action cannot be undone.");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                DatabaseManager.deleteLibrarianFromDatabase(selectedLibrarian);
                librarianList.remove(selectedLibrarian);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Deleted");
                successAlert.setHeaderText("Librarian Deleted");
                successAlert.setContentText("The librarian \"" + selectedLibrarian.getName() + "\" has been deleted successfully.");
                successAlert.showAndWait();
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to Delete Librarian");
                errorAlert.setContentText("There was an error deleting the librarian from the database. Please try again.");
                errorAlert.showAndWait();
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
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
        empIdField.clear();
    }
}