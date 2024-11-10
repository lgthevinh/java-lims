package view.controller;

import com.lims.dao.DatabaseManager;
import com.lims.model.Librarian;
import com.lims.model.Student;
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
        addressLineColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        schoolColumn.setCellValueFactory(new PropertyValueFactory<>("school"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dateOfBirthColumn.setCellFactory(column -> {
            return new TableCell<Student, Date>() {
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
            studentList.addAll(DatabaseManager.getAllStudents());
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddStudent() {
        try {
            String socialId = socialIdField.getText();
            String studentId = studentIdField.getText();
            String school = schoolField.getText();
            String major = majorField.getText();

            if (socialId.isEmpty() || studentId.isEmpty() || school.isEmpty() || major.isEmpty()) {
                System.out.println("All fields are required!");
                return;
            }

            User user = DatabaseManager.getUserBySocialId(socialId);

            if (user == null) {
                System.out.println("User not found!");
                return;
            }

            DatabaseManager.addStudentToDatabase(user, studentId, school, major);

            Student student = new Student(user);
            studentList.add(student);
            clearFields();
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
    private void handleDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {
                DatabaseManager.deleteStudentFromDatabase(selectedStudent);
                studentList.remove(selectedStudent);
            } catch (SQLException e) {
                System.out.println("An error occurred while accessing the database.");
                e.printStackTrace();
            }
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