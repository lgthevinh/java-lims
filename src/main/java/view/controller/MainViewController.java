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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
public class MainViewController {
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
    private TableColumn<Book, Integer> availableAmountColumn;
    @FXML
    private TextField searchField;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
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
        bookTable.setItems(bookList);
        try {
            bookList.addAll(DatabaseManager.getAllBooks());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public void setBookList(List<Book> books) {
        bookList.setAll(books);
    }
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        List<Book> filteredBooks = bookList.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchText) ||
                        book.getAuthor().toLowerCase().contains(searchText) ||
                        book.getIsbn().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        bookTable.setItems(FXCollections.observableArrayList(filteredBooks));
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleManageBooks(ActionEvent event) {
        loadView("/fxml/BookView.fxml", event);
    }
    @FXML
    public void handleManageBorrowDetails(ActionEvent event) {
        loadView("/fxml/BorrowDetailView.fxml", event);
    }
    @FXML
    public void handleManageLibrarians(ActionEvent event) {
        loadView("/fxml/LibrarianView.fxml", event);
    }
    @FXML
    public void handleManageStudents(ActionEvent event) {
        loadView("/fxml/StudentView.fxml", event);
    }
    @FXML
    public void handleManageUsers(ActionEvent event) {
        loadView("/fxml/UserView.fxml", event);
    }
    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
    private void loadView(String fxmlPath, ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}