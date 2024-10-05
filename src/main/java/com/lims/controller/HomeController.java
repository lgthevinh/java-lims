package com.lims.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;

import static com.lims.DatabaseManager.getAllBooks;

public class HomeController {
    @FXML
    private ListView book_list;

    @FXML
    public void initialize() throws SQLException {
        System.out.println("Run initializer");
        book_list.getItems().addAll(getAllBooks());
    }
}