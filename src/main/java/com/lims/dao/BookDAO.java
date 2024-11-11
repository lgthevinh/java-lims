package com.lims.dao;

import com.lims.model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.lims.Utils.convertStringToDatetime;
import static com.lims.Utils.formatDatetime;

public class BookDAO extends DatabaseManager {

    public static List<Book> getAllBooks() throws SQLException, ParseException {
        List<Book> books = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Book");

        while (resultSet.next()) {
            Book book = new Book(
                    resultSet.getString("isbn"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("year_of_publication")),
                    resultSet.getString("publisher"),
                    resultSet.getString("image_url"),
                    resultSet.getInt("available_amount")
            );
            books.add(book);
        }
        conn.close();
        return books;
    }

    public static Book getBookByISBN(String isbn) throws SQLException, ParseException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Book WHERE isbn = '%s'".formatted(isbn));

        if (resultSet.next()) {
            Book book = new Book(
                    resultSet.getString("isbn"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("year_of_publication")),
                    resultSet.getString("publisher"),
                    resultSet.getString("image_url"),
                    resultSet.getInt("available_amount")
            );
            conn.close();
            return book;
        }

        conn.close();
        return null;
    }

    public static void addBookToDatabase(Book book) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO Book VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d)".formatted(
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                formatDatetime("MM-dd-yyyy", book.getYearOfPublication()),
                book.getPublisher(),
                book.getImageUrl(),
                book.getAvailableAmount()
        ));
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
                formatDatetime("MM-dd-yyyy", book.getYearOfPublication()),
                book.getPublisher(),
                book.getImageUrl(),
                book.getAvailableAmount(),
                book.getIsbn()
        ));
        conn.close();
    }

}
