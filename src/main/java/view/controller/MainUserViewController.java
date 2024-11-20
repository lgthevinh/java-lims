package view.controller;
import animatefx.animation.FadeIn;
import com.lims.dao.DatabaseManager;
import com.lims.model.Book;
import javafx.animation.FadeTransition;
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
import javafx.util.Duration;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
public class MainUserViewController {
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
        // Tạo Context Menu cho ISBN
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyIsbnItem = new MenuItem("Copy ISBN");

        // Gắn hành động copy khi chọn menu
        copyIsbnItem.setOnAction(event -> {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                copyToClipboard(selectedBook.getIsbn());
            } else {
                System.out.println("No book selected");
            }
        });

        contextMenu.getItems().add(copyIsbnItem);

        // Gắn Context Menu vào mỗi cell của cột ISBN
        isbnColumn.setCellFactory(column -> {
            TableCell<Book, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);

                    // Chỉ gắn Context Menu nếu cell không rỗng
                    if (!empty) {
                        setContextMenu(contextMenu);
                    }
                }
            };
            return cell;
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
    @FXML
    private void copyToClipboard(String text) {
        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
        javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
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
            FadeIn fadeIn = new FadeIn(root);
            fadeIn.setSpeed(1.0);
            fadeIn.play();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleManageUsers(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserInforView.fxml"));
        loadView(loader, event);
    }

    @FXML
    public void handleBorrowBook(ActionEvent event) throws SQLException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BorrowBookView.fxml"));
        loadView(loader, event);
        BorrowBookController controller = loader.getController();
        controller.setBookList(FXCollections.observableArrayList(DatabaseManager.getAllBooks()));
    }
    public ObservableList<Book> getBookList() {
        return bookList;
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }

    private void loadView(FXMLLoader loader, ActionEvent event) {
        try {
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(500));
            fadeTransition.setNode(root);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}