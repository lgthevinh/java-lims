package view.controller;
import animatefx.animation.ZoomIn;
import com.lims.dao.DatabaseManager;
import com.lims.model.Book;
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
public class BookController {
    @FXML
    TableView<Book> bookTable;
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
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        imageUrlColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        availableAmountColumn.setCellValueFactory(new PropertyValueFactory<>("availableAmount"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        yearOfPublicationColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfPublication"));
        yearOfPublicationColumn.setCellFactory(column -> {
            return new TableCell<Book, Date>() {
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
        // Load books from the database
        try {
            bookList.addAll(DatabaseManager.getAllBooks());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        bookTable.setOnMouseClicked(event -> handleRowSelect());
    }

    public void addBookFromApi(Book book) {
        bookList.add(book);
        try {
            DatabaseManager.addBookToDatabase(book);
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
        Integer availableAmount = null;
        try {
            availableAmount = Integer.parseInt(availableAmountField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Amount");
            alert.setContentText("Please enter a valid number for available amount.");
            alert.showAndWait();
            return;
        }

        // Kiểm tra sách trùng ISBN
        boolean isDuplicate = bookList.stream().anyMatch(book -> book.getIsbn().equals(isbn));
        if (isDuplicate) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Book");
            alert.setHeaderText("Book Already Exists");
            alert.setContentText("A book with ISBN \"" + isbn + "\" already exists in the system.");
            alert.showAndWait();
            return;
        }

        // Tạo đối tượng Book mới
        Book newBook = new Book(isbn, title, author, yearOfPublication, publisher, imageUrl, availableAmount);

        // Lưu sách vào cơ sở dữ liệu
        try {
            DatabaseManager.addBookToDatabase(newBook);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to Add Book");
            alert.setContentText("There was an error adding the book to the database. Please try again.");
            alert.showAndWait();
            return;
        }

        // Thêm sách vào danh sách hiển thị
        bookList.add(newBook);
        clearFields();
    }


    @FXML
    private void handleAddBookByApi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchBookByApiView.fxml"));
            Parent root = loader.load();
            SearchBookByApiController controller = loader.getController();
            controller.setBookController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Search Book by API");
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Are you sure you want to delete this book?");
        confirmAlert.setContentText("Book Title: " + selectedBook.getTitle() + "\nThis action cannot be undone.");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            bookList.remove(selectedBook);
            try {
                DatabaseManager.deleteBookFromDatabase(selectedBook.getIsbn());
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Deleted");
                successAlert.setHeaderText("Book Deleted");
                successAlert.setContentText("The book \"" + selectedBook.getTitle() + "\" has been deleted successfully.");
                successAlert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to Delete Book");
                errorAlert.setContentText("There was an error deleting the book from the database. Please try again.");
                errorAlert.showAndWait();
            }
        }
    }


    @FXML
    private void handleUpdateBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            selectedBook.setTitle(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            if (yearOfPublicationField.getValue() != null) {
                selectedBook.setYearOfPublication(java.sql.Date.valueOf(yearOfPublicationField.getValue()));
            }
            selectedBook.setPublisher(publisherField.getText());
            selectedBook.setImageUrl(imageUrlField.getText());
            selectedBook.setAvailableAmount(Integer.parseInt(availableAmountField.getText()));

            try {
                DatabaseManager.updateBookInDatabase(selectedBook);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            bookTable.refresh();
            clearFields();
        }
    }

    @FXML
    private void handleRowSelect() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            isbnField.setText(selectedBook.getIsbn());
            titleField.setText(selectedBook.getTitle());
            authorField.setText(selectedBook.getAuthor());

            if (selectedBook.getYearOfPublication() != null) {
                yearOfPublicationField.setValue(selectedBook.getYearOfPublication().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
            }

            publisherField.setText(selectedBook.getPublisher());
            imageUrlField.setText(selectedBook.getImageUrl());
            availableAmountField.setText(String.valueOf(selectedBook.getAvailableAmount()));
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
        isbnField.clear();
        titleField.clear();
        authorField.clear();
        yearOfPublicationField.setValue(null);
        publisherField.clear();
        imageUrlField.clear();
        availableAmountField.clear();
    }
}