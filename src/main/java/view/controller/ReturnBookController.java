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
public class ReturnBookController {
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
    private void returnBook() {
        BorrowDetail selectedBorrowDetail = borrowDetailTable.getSelectionModel().getSelectedItem();

        if (selectedBorrowDetail == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Borrow Detail Selected");
            alert.setContentText("Please select a borrow detail to return the book.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Return");
        confirmAlert.setHeaderText("Are you sure you want to return this book?");
        confirmAlert.setContentText("This action will update the actual return date.");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                Date actualReturnDate = new Date();
                selectedBorrowDetail.setActualReturnDate(actualReturnDate);

                BorrowDetailDAO.updateBorrowDetailInDatabase(selectedBorrowDetail);

                String bookIsbn = selectedBorrowDetail.getBookIsbn();
                Book book = bookList.stream()
                        .filter(b -> b.getIsbn().equals(bookIsbn))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Book not found!"));
                book.setAvailableAmount(book.getAvailableAmount() + 1);
                DatabaseManager.updateBookInDatabase(book);

                borrowDetailTable.refresh();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Return Successful");
                successAlert.setHeaderText("Book Returned Successfully");
                successAlert.setContentText("The book has been returned with the actual return date updated.");
                successAlert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Database Error");
                errorAlert.setContentText("Failed to update the actual return date. Please try again.");
                errorAlert.showAndWait();
            }
        }
    }


    public void setBorrowDetailList(ObservableList<BorrowDetail> borrowDetails) {
        this.borrowDetailList = borrowDetails;
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