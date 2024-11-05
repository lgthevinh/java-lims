package view.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CatalogController {

    @FXML
    private VBox vboxContainer;
    @FXML
    private Label nameLabel; // Label cho tên
    @FXML
    private Label idLabel; // Label cho ID
    @FXML
    private Label amountLabel; // Label cho số lượng
    @FXML
    private Label dueDateLabel; // Label cho ngày đến hạn
    @FXML
    private Label dateTimeLabel; // Label cho ngày giờ
    public void preloadData(List<String[]> data) {
        Thread preloadThread = new Thread(() -> {
            for (String[] d : data) {
                loadBorrowedBookBar(d);
                try {
                    Thread.sleep(10); // Tạm dừng một chút giữa các lần nạp
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        preloadThread.start();
    }

    public void loadBorrowedBookBar(String[] d) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/InforCatalog.fxml"));
            HBox userHBox = fxmlLoader.load(); // Chú ý là chúng ta tạo HBox

            CatalogController controller = fxmlLoader.getController();
            controller.setData(d[0], d[1], Integer.parseInt(d[2]), d[3], d[4]);

            Platform.runLater(() -> vboxContainer.getChildren().add(userHBox)); // Thêm HBox vào VBox
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức này trả về dữ liệu mẫu
    public List<String[]> getSampleUsers() {
        return List.of(
                new String[]{"Momo Ayase", "23020696", "2", "3-11-2024", "24-10-2024"},
                new String[]{"Yuki Takami", "23020697", "1", "4-11-2024", "25-10-2024"}
                // Thêm nhiều người dùng hơn nếu cần
        );
    }

    public void setData(String name, String id, int amount, String dueDate, String dateTime) {
        nameLabel.setText(name);
        idLabel.setText(id);
        amountLabel.setText(String.valueOf(amount));
        dueDateLabel.setText(dueDate);
        dateTimeLabel.setText(dateTime);
    }
}
