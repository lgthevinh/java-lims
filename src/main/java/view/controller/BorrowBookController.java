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
import java.time.ZoneId;
import java.util.Date;
public class BorrowBookController {
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
    private TextField bookIsbnField;
    @FXML
    private TextField borrowerIdField;
    @FXML
    private TextField librarianIdField;
    @FXML
    private DatePicker borrowDateField;
    @FXML
    private DatePicker expectedReturnDateField;

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
        try {
            // Load the book list from the database
            borrowDetailList.addAll(DatabaseManager.getAllBorrowDetail());
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        borrowDetailTable.setOnMouseClicked(event -> handleRowSelect());
    }
    @FXML
    private void handleBorrowBook() {
        try {
            String bookIsbn = bookIsbnField.getText();
            Integer borrowerId = Integer.parseInt(borrowerIdField.getText());
            Integer librarianId = null;
            Date borrowDate = null;
            Date expectedReturnDate = null;
            if (borrowDateField.getValue() != null) {
                borrowDate = java.sql.Date.valueOf(borrowDateField.getValue());
            }
            if (expectedReturnDateField.getValue() != null) {
                expectedReturnDate = java.sql.Date.valueOf(expectedReturnDateField.getValue());
            }
            BorrowDetail newBorrowDetail = new BorrowDetail(
                    bookIsbn,
                    borrowerId,
                    null,
                    borrowDate,
                    expectedReturnDate,
                    null
            );

            BorrowDetailDAO.addBorrowDetailToDatabase(newBorrowDetail);
            borrowDetailList.add(newBorrowDetail);
            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBookList(ObservableList<Book> bookList) {
        this.bookList = bookList;
    }

    @FXML
    private void handleRowSelect() {
        BorrowDetail selectedBorrowDetail = borrowDetailTable.getSelectionModel().getSelectedItem();
        if (selectedBorrowDetail != null) {
            bookIsbnField.setText(selectedBorrowDetail.getBookIsbn());
            borrowerIdField.setText(selectedBorrowDetail.getBorrowerId().toString());
            if (selectedBorrowDetail.getBorrowDate() != null) {
                borrowDateField.setValue(selectedBorrowDetail.getBorrowDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
            }
            if (selectedBorrowDetail.getExpectedReturnDate() != null) {
                expectedReturnDateField.setValue(selectedBorrowDetail.getExpectedReturnDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
            }
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
        bookIsbnField.clear();
        borrowerIdField.clear();
        borrowDateField.setValue(null);
        expectedReturnDateField.setValue(null);
    }
}