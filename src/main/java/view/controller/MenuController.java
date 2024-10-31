package view.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane contentPane;

    private Button activeButton; // Variable to store the current button

    @FXML
    public void initialize() {
        // Set initial state for the button
        activeButton = dashboardButton;
        updateButtonStyles(activeButton); // Update button style

        // Assign events to buttons
        setupButtonStyles(catalogButton, "/fxml/HomeCatalog.fxml");
        setupButtonStyles(dashboardButton, "/fxml/HomeDashboard.fxml");
        setupButtonStyles(booksButton, "/fxml/HomeBooks.fxml");
        setupButtonStyles(usersButton, "/fxml/HomeUsers.fxml");
    }

    private void setupButtonStyles(Button button, String fxmlPath) {
        button.setOnAction(event -> {
            if (button != activeButton) { // Only load new content if a different button is selected
                activeButton = button; // Update the active button
                updateButtonStyles(activeButton); // Update button style
                loadContent(fxmlPath); // Load new content
            }
        });
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newContent = loader.load();
            contentPane.getChildren().setAll(newContent); // Ensure contentPane is not null
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateButtonStyles(Button activeButton) {
        Button[] buttons = {catalogButton, dashboardButton, booksButton, usersButton};

        for (Button button : buttons) {
            button.getStyleClass().removeAll("button-style-active", "button-style-inactive");
            if (button == activeButton) {
                button.getStyleClass().add("button-style-active");
            } else {
                button.getStyleClass().add("button-style-inactive");
            }
        }
    }
}