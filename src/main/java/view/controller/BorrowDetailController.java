package view.controller;
import com.lims.model.BorrowDetail;
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
public class BorrowDetailController {
    @FXML
    private TableView<BorrowDetail> borrowDetailTable;
    @FXML
    private TableColumn<BorrowDetail, String> bookIsbnColumn;
    @FXML
    private TableColumn<BorrowDetail, Integer> borrowerIdColumn;
    @FXML
    private TableColumn<BorrowDetail, Integer> librarianIdColumn;
    @FXML
    private TableColumn<BorrowDetail, Date> borrowDateColumn;
    @FXML
    private TableColumn<BorrowDetail, Date> expectedReturnDateColumn;
    @FXML
    private TableColumn<BorrowDetail, Date> actualReturnDateColumn;
    @FXML
    private TextField bookIsbnField;
    @FXML
    private TextField borrowerIdField;
    @FXML
    private TextField librarianIdField;
    @FXML
    private DatePicker borrowDateField;
    @FXML
    private DatePicker expectedReturnDateField;
    @FXML
    private DatePicker actualReturnDateField;
    private ObservableList<BorrowDetail> borrowDetailList = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        // Initialize the borrow detail table with the borrow detail list
        borrowDetailTable.setItems(borrowDetailList);
        // Set up the columns in the table
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("bookIsbn"));
        borrowerIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrowerId"));
        librarianIdColumn.setCellValueFactory(new PropertyValueFactory<>("librarianId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        expectedReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedReturnDate"));
        actualReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("actualReturnDate"));
    }
    @FXML
    private void handleAddBorrowDetail() {
        String bookIsbn = bookIsbnField.getText();
        Integer borrowerId = Integer.parseInt(borrowerIdField.getText());
        Integer librarianId = Integer.parseInt(librarianIdField.getText());
        Date borrowDate = null;
        Date expectedReturnDate = null;
        Date actualReturnDate = null;
        if (borrowDateField.getValue() != null) {
            borrowDate = java.sql.Date.valueOf(borrowDateField.getValue());
        }
        if (expectedReturnDateField.getValue() != null) {
            expectedReturnDate = java.sql.Date.valueOf(expectedReturnDateField.getValue());
        }
        if (actualReturnDateField.getValue() != null) {
            actualReturnDate = java.sql.Date.valueOf(actualReturnDateField.getValue());
        }
        BorrowDetail newBorrowDetail = new BorrowDetail(bookIsbn, borrowerId, librarianId, borrowDate, expectedReturnDate, actualReturnDate);
        borrowDetailList.add(newBorrowDetail);
        clearFields();
    }
    @FXML
    private void handleDeleteBorrowDetail() {
        BorrowDetail selectedBorrowDetail = borrowDetailTable.getSelectionModel().getSelectedItem();
        if (selectedBorrowDetail != null) {
            borrowDetailList.remove(selectedBorrowDetail);
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
        bookIsbnField.clear();
        borrowerIdField.clear();
        librarianIdField.clear();
        borrowDateField.setValue(null);
        expectedReturnDateField.setValue(null);
        actualReturnDateField.setValue(null);
    }
}