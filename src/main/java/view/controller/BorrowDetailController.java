package view.controller;
import animatefx.animation.ZoomIn;
import com.lims.dao.BorrowDetailDAO;
import com.lims.dao.DatabaseManager;
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

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class BorrowDetailController {
    @FXML
    private TableView<BorrowDetail> borrowDetailTable;
    @FXML
    private TableColumn<BorrowDetail, String> bookIsbnColumn;
    @FXML
    private TableColumn<BorrowDetail, String> titleColumn;
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
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<BorrowDetail> borrowDetailList = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        try {
            bookList.addAll(DatabaseManager.getAllBooks());
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // Initialize the borrow detail table with the borrow detail list
        borrowDetailTable.setItems(borrowDetailList);
        // Set up the columns in the table
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("bookIsbn"));
        titleColumn.setCellValueFactory(cellData->{
            BorrowDetail borrowDetail = cellData.getValue();
            String bookIsbn = borrowDetail.getBookIsbn();

            String title = bookList.stream()
                    .filter(book -> book.getIsbn().equals(bookIsbn))
                    .findFirst()
                    .map(Book::getTitle)
                    .orElse(null);

            return javafx.beans.binding.Bindings.createObjectBinding(() -> title);
        });
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
        actualReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("actualReturnDate"));
        actualReturnDateColumn.setCellFactory(column -> {
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
        try {
            // Load the book list from the database
            borrowDetailList.addAll(DatabaseManager.getAllBorrowDetail());
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
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
        bookIsbnField.clear();
        borrowerIdField.clear();
        borrowDateField.setValue(null);
        expectedReturnDateField.setValue(null);
    }
}