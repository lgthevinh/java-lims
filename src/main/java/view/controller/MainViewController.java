package view.controller;
import animatefx.animation.FadeIn;
import com.lims.dao.DatabaseManager;
import  view.controller.GenerateQRController;
import com.lims.model.Book;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import javafx.util.Duration;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.paint.Color;

import static view.controller.GenerateQRController.generateQRCode;

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
    private void handleShowQR() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {

            String qrContent = String.format("https://books.google.com/books?vid=ISBN:%s", selectedBook.getIsbn());

            WritableImage qrImage = generateQRCode(qrContent, 250, 250);
            ImageView qrImageView = new ImageView(qrImage);

            Stage qrStage = new Stage();
            qrStage.centerOnScreen();
            qrStage.initModality(Modality.APPLICATION_MODAL);
            qrStage.setTitle("QR Code");

            VBox qrVBox = new VBox(10);
            qrVBox.setAlignment(Pos.CENTER);
            qrVBox.getChildren().add(qrImageView);

            Scene qrScene = new Scene(qrVBox, 300, 300);
            qrStage.setScene(qrScene);

            Stage mainStage = (Stage) bookTable.getScene().getWindow();
            mainStage.centerOnScreen();
            qrStage.setX(mainStage.getX() + (mainStage.getWidth() - qrStage.getWidth()) / 2);
            qrStage.setY(mainStage.getY() + (mainStage.getHeight() - qrStage.getHeight()) / 2);

            qrStage.show();
        }
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