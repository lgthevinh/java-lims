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
        borrowDetailTable.setOnMouseClicked(event -> handleRowSelect());
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
    @FXML
    private void handleDeleteDetail() {
        BorrowDetail selectedBorrowDetail = borrowDetailTable.getSelectionModel().getSelectedItem();

        if (selectedBorrowDetail == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Borrow Detail Selected");
            alert.setContentText("Please select a borrow detail to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Are you sure you want to delete this borrow detail?");
        confirmAlert.setContentText("This action cannot be undone.");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                BorrowDetailDAO.deleteBorrowDetailFromDatabase(selectedBorrowDetail);
                borrowDetailList.remove(selectedBorrowDetail);
                borrowDetailTable.refresh();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Deleted");
                successAlert.setHeaderText("Borrow Detail Deleted");
                successAlert.setContentText("The selected borrow detail has been successfully deleted.");
                successAlert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Database Error");
                errorAlert.setContentText("Failed to delete the borrow detail. Please try again.");
                errorAlert.showAndWait();
            }
        }
    }

    @FXML
    private void handleUpdateDetail() {
        BorrowDetail selectedBorrowDetail = borrowDetailTable.getSelectionModel().getSelectedItem();
        if (selectedBorrowDetail == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Borrow Detail Selected");
            alert.setContentText("Please select a borrow detail to update.");
            alert.showAndWait();
            return;
        }

        try {
            BorrowDetail updatedBorrowDetail = new BorrowDetail(
                    bookIsbnField.getText(),
                    Integer.parseInt(borrowerIdField.getText()),
                    Integer.parseInt(librarianIdField.getText()),
                    borrowDateField.getValue() != null ? Date.from(borrowDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null,
                    expectedReturnDateField.getValue() != null ? Date.from(expectedReturnDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null,
                    actualReturnDateField.getValue() != null ? Date.from(actualReturnDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null
            );
            updatedBorrowDetail.setId(selectedBorrowDetail.getId());
            BorrowDetailDAO.updateBorrowDetailInDatabase(updatedBorrowDetail);
            borrowDetailList.set(borrowDetailList.indexOf(selectedBorrowDetail), updatedBorrowDetail);
            borrowDetailTable.refresh();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Update Borrow Detail");
            alert.setContentText("There was an error updating the borrow detail. Please try again.");
            alert.showAndWait();
        }
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
            if (selectedBorrowDetail.getActualReturnDate() != null) {
                actualReturnDateField.setValue(selectedBorrowDetail.getActualReturnDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
            }
        }
    }

    private void clearFields() {
        bookIsbnField.clear();
        borrowerIdField.clear();
        borrowDateField.setValue(null);
        expectedReturnDateField.setValue(null);
    }
}