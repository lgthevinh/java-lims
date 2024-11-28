package view.controller;

import com.lims.dao.DatabaseManager;
import view.controller.BookController;
import com.lims.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class SearchBookByApiController {
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
    private TextField searchField;
    @FXML
    private Button  searchButton;
    @FXML
    private Button addSelectedBookButton;
    @FXML
    private Button cancelButton;

    private BookController bookController;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
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
        bookTable.setItems(bookList);
    }

    @FXML
    private void searchBookByApi() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Search field cannot be empty!");
            return;
        }

        String apiKey = "AIzaSyCHhMgtREyCQjs6DMmU8T0VzdsfPtykspI";
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=40&key=" + apiKey;

        try {
            // Send GET request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Thread.sleep(2000);
            if (response.statusCode() == 200) {
                // Parse JSON response
                String responseBody = response.body();
                List<Book> books = parseBooksFromGoogleJson(responseBody);

                // Update TableView
                bookList.setAll(books);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch data from API. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    private List<Book> parseBooksFromGoogleJson(String json) {
        List<Book> books = new ArrayList<>();
        Gson gson = new Gson();

        try {
            // Parse the JSON structure
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonArray items = root.getAsJsonArray("items");

            for (JsonElement element : items) {
                JsonObject item = element.getAsJsonObject();
                JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

                String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Unknown Title";
                String isbn = "N/A"; // Default value
                if (volumeInfo.has("industryIdentifiers")) {
                    JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
                    for (JsonElement id : identifiers) {
                        JsonObject idObject = id.getAsJsonObject();
                        if (idObject.get("type").getAsString().equals("ISBN_13")) {
                            isbn = idObject.get("identifier").getAsString();
                            break;
                        }
                    }
                }

                String author = volumeInfo.has("authors") ?
                        volumeInfo.get("authors").getAsJsonArray().get(0).getAsString() : "Unknown Author";
                Date yearOfPublication = parseYear(volumeInfo.has("publishedDate") ?
                        volumeInfo.get("publishedDate").getAsString() : null);
                String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown Publisher";
                String imageUrl = volumeInfo.has("imageLinks") ?
                        volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : null;

                books.add(new Book(isbn, title, author, yearOfPublication, publisher, imageUrl, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse API response.");
        }

        return books;
    }

    private Date parseYear(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(dateStr);
        } catch (ParseException e) {
            try {
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                return yearFormat.parse(dateStr);
            } catch (ParseException ex) {
                return null; // Default to null if parsing fails
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setBookController(BookController bookController) {
        this.bookController = bookController;
    }

    @FXML
    private void addSelectedBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to add.");
            return;
        }

        // Kiểm tra trùng ISBN trong BookController
        for (Book book : bookController.bookTable.getItems()) {
            if (book.getIsbn().equals(selectedBook.getIsbn())) {
                showAlert(Alert.AlertType.WARNING, "Duplicate Book", "A book with the same ISBN already exists in the library.");
                return;
            }
        }

        // Thêm sách vào danh sách của BookController
        bookController.addBookFromApi(selectedBook);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully!");
    }


    @FXML
    private void cancel() {
        Stage stage = (Stage) bookTable.getScene().getWindow();
        stage.close();
    }
}
