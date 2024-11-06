package view.controller;
import com.lims.dao.DatabaseManager;
import com.lims.model.Librarian;
import com.lims.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        addressLineColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        empIdColumn.setCellValueFactory(new PropertyValueFactory<>("empId"));

        try {
            librarianList.addAll(DatabaseManager.getAllLibrarian());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void handleAddLibrarian() {
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
        Integer empId = Integer.parseInt(empIdField.getText());
        User user = new User(socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);
        Librarian newLibrarian = new Librarian(user);
        newLibrarian.setEmpId(empId);
        librarianList.add(newLibrarian);

    }
    @FXML
    private void handleDeleteLibrarian() {
        Librarian selectedLibrarian = librarianTable.getSelectionModel().getSelectedItem();
        if (selectedLibrarian != null) {
            librarianList.remove(selectedLibrarian);
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
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
        empIdField.clear();
    }
}