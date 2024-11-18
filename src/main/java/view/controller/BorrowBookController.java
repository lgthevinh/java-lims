package view.controller;
import animatefx.animation.ZoomIn;
import com.lims.model.Book;
import com.lims.model.BorrowDetail;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class BorrowBookController {
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
    private ObservableList<Book> bookList;
    private ObservableList<BorrowDetail> borrowDetailList = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        // Initialize the borrow detail table with the borrow detail list
        borrowDetailTable.setItems(borrowDetailList);
        // Set up the columns in the table
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("bookIsbn"));
        borrowerIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrowerId"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        borrowDateColumn.setCellFactory(column -> {
            return new TableCell<BorrowDetail, Date>() {
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
        expectedReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedReturnDate"));
        expectedReturnDateColumn.setCellFactory(column -> {
            return new TableCell<BorrowDetail, Date>() {
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
    }
    @FXML
    private void handleBorrowBook() {
        String bookIsbn = bookIsbnField.getText();
        Integer borrowerId = Integer.parseInt(borrowerIdField.getText());
        Integer librarianId = null;
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
    public void setBookList(ObservableList<Book> bookList) {
        this.bookList = bookList;
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
        bookIsbnField.clear();
        borrowerIdField.clear();
        librarianIdField.clear();
        borrowDateField.setValue(null);
        expectedReturnDateField.setValue(null);
        actualReturnDateField.setValue(null);
    }
}