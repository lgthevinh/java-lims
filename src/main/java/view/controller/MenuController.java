package view.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button catalogButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button booksButton;

    @FXML
    private Button usersButton;

    @FXML
    public void initialize() {
        // Thiết lập chức năng cho từng nút
        setupButtonStyles(catalogButton, "/fxml/HomeCatalog.fxml");
        setupButtonStyles(dashboardButton, "/fxml/HomeDashboard.fxml");
        setupButtonStyles(booksButton, "/fxml/HomeBooks.fxml");
        setupButtonStyles(usersButton, "/fxml/HomeUsers.fxml");
    }

    private void setupButtonStyles(Button button, String fxmlPath) {
        button.setOnAction(event -> {
            // Đặt kiểu `button1` cho nút được nhấn
            applyButtonStyles(button);

            // Chuyển cảnh
            try {
                Parent parent = FXMLLoader.load(getClass().getResource(fxmlPath));
                Scene scene = new Scene(parent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void applyButtonStyles(Button activeButton) {
        // Tạo một mảng các nút để dễ dàng lặp lại
        Button[] buttons = {catalogButton, dashboardButton, booksButton, usersButton};

        for (Button button : buttons) {
            if (button == activeButton) {
                // Thêm các lớp cho nút được nhấn
                button.getStyleClass().removeAll("button-style-inactive");
                button.getStyleClass().addAll("button-style-active");
            } else {
                // Thêm các lớp cho các nút không được nhấn
                button.getStyleClass().removeAll("button-style-active");
                button.getStyleClass().addAll("button-style-inactive");
            }
        }
    }
}
