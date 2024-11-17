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
            String empId = empIdField.getText();

            if (socialId.isEmpty() || empId.isEmpty()) {
                System.out.println("Both fields are required!");
                return;
            }

            User user = DatabaseManager.getUserBySocialId(socialId);

            if (user == null) {
                System.out.println("User not found!");
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
        if (selectedLibrarian != null) {
            try {
                DatabaseManager.deleteLibrarianFromDatabase(selectedLibrarian);
                librarianList.remove(selectedLibrarian);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
        passwordField.clear();
        empIdField.clear();
    }
}