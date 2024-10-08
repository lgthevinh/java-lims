package com.lims.dao;

import com.lims.model.Book;
import com.lims.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.lims.Utils.formatDatetime;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:./src/main/resources/database/db.sqlite";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Book");

        while (resultSet.next()) {
            Book book = new Book(
                    resultSet.getString("isbn"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getDate("year_of_publication"),
                    resultSet.getString("publisher"),
                    resultSet.getString("image_url"),
                    resultSet.getInt("available_amount")
            );
            books.add(book);
        }
        conn.close();
        return books;
    }

    public static Book getBookByISBN(Integer isbn) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Book WHERE isbn = " + isbn);

        if (resultSet.first()) {
            conn.close();
            return new Book(
                    resultSet.getString("isbn"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getDate("year_of_publication"),
                    resultSet.getString("publisher"),
                    resultSet.getString("image_url"),
                    resultSet.getInt("available_amount")
            );
        }
        conn.close();
        return null;
    }

    public static void addBookToDatabase(Book book) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        String sqlStatement = "INSERT INTO Book VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d)".formatted(
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                formatDatetime("yyyy", book.getYearOfPublication()),
                book.getPublisher(),
                book.getImageUrl(),
                book.getAvailableAmount()
        );
        System.out.println(sqlStatement);
        statement.executeUpdate(sqlStatement);
        conn.close();
    }

    public static void deleteBookFromDatabase(String book_isbn) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("DELETE FROM Book WHERE isbn = '%s'".formatted(book_isbn));
        conn.close();
    }

    public static void updateBookInDatabase(Book book) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("UPDATE Book SET title = '%s', author = '%s', year_of_publication = '%s', publisher = '%s', image_url = '%s', available_amount = '%d' WHERE isbn = '%s'".formatted(
                book.getTitle(),
                book.getAuthor(),
                formatDatetime("yyyy", book.getYearOfPublication()),
                book.getPublisher(),
                book.getImageUrl(),
                book.getAvailableAmount(),
                book.getIsbn()
        ));
        conn.close();
    }

    public static List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM User");

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("social_id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date_of_birth"),
                    resultSet.getString("address_line"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
            user.setUserId(resultSet.getInt("id"));
            userList.add(user);
        }

        conn.close();
        return userList;
    }
}
