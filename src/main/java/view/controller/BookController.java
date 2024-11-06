package view.controller;
import com.lims.dao.DatabaseManager;
import com.lims.model.Book;
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
import java.util.Date;
public class BookController {
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Date> yearOfPublicationColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, String> imageUrlColumn;
    @FXML
    private TableColumn<Book, Integer> availableAmountColumn;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private DatePicker yearOfPublicationField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField imageUrlField;
    @FXML
    private TextField availableAmountField;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        // Initialize the book table with the book list
        bookTable.setItems(bookList);
        // Set up the columns in the table
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        yearOfPublicationColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfPublication"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        imageUrlColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        availableAmountColumn.setCellValueFactory(new PropertyValueFactory<>("availableAmount"));
        // Load books from the database
        try {
            bookList.addAll(DatabaseManager.getAllBooks());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAddBook() {
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        Date yearOfPublication = null;
        if (yearOfPublicationField.getValue() != null) {
            yearOfPublication = java.sql.Date.valueOf(yearOfPublicationField.getValue());
        }
        String publisher = publisherField.getText();
        String imageUrl = imageUrlField.getText();
        Integer availableAmount = Integer.parseInt(availableAmountField.getText());
        Book newBook = new Book(isbn, title, author, yearOfPublication, publisher, imageUrl, availableAmount);
        // Save the book to the database
        try {
            DatabaseManager.addBookToDatabase(newBook);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Add the book to the observable list
        bookList.add(newBook);
        clearFields();
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            bookList.remove(selectedBook);
            try {
                DatabaseManager.deleteBookFromDatabase(selectedBook.getIsbn());
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
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void clearFields() {
        isbnField.clear();
        titleField.clear();
        authorField.clear();
        yearOfPublicationField.setValue(null);
        publisherField.clear();
        imageUrlField.clear();
        availableAmountField.clear();
    }
}