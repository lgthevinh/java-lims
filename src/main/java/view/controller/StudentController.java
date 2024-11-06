package view.controller;

import com.lims.model.Student;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentController {
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> socialIdColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, Date> dateOfBirthColumn;
    @FXML
    private TableColumn<Student, String> addressLineColumn;
    @FXML
    private TableColumn<Student, String> phoneNumberColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TableColumn<Student, String> studentIdColumn;
    @FXML
    private TableColumn<Student, String> schoolColumn;
    @FXML
    private TableColumn<Student, String> majorColumn;
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
    private TextField studentIdField;
    @FXML
    private TextField schoolField;
    @FXML
    private TextField majorField;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize the student table with the student list
        studentTable.setItems(studentList);

        // Set up the columns in the table
        socialIdColumn.setCellValueFactory(new PropertyValueFactory<>("socialId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        addressLineColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        schoolColumn.setCellValueFactory(new PropertyValueFactory<>("school"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
    }

    @FXML
    private void handleAddStudent() {
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
        String studentId = studentIdField.getText();
        String school = schoolField.getText();
        String major = majorField.getText();

        User user = new User(socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);
        Student newStudent = new Student(user);
        newStudent.setStudentId(studentId);
        newStudent.setSchool(school);
        newStudent.setMajor(major);

        studentList.add(newStudent);
        clearFields();
    }

    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            studentList.remove(selectedStudent);
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
        studentIdField.clear();
        schoolField.clear();
        majorField.clear();
    }
}