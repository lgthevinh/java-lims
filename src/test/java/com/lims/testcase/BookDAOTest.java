package com.lims.testcase;

import com.lims.Utils;
import com.lims.dao.DatabaseManager;
import com.lims.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookDAOTest {

    Book initializeTestBook1() {
        try {
            return new Book(
                    "9786043456905",
                    "You are not so smart (Vietnamese)",
                    "David McRaney",
                    Utils.convertStringToDatetime("yyyy-MM-dd", "2022-1-1"),
                    "R. E. A. D. books",
                    "https://books.google.com.vn/books/publisher/content?id=JvnDEAAAQBAJ&hl=vi&pg=PA1&img=1&zoom=3&sig=ACfU3U0kE6m-j2Rqj3BQ4K6lV2v26ANwGw&w=1280",
                    5
            );
        } catch (ParseException e) {
            System.out.println("Error parsing date");
            return null;
        }
    }

    Book initializeTestBook2() {
        try {
            return new Book(
                    "9786043652307",
                    "Negotiation (Vietnamese)",
                    "Brian Tracy",
                    Utils.convertStringToDatetime("yyyy-MM-dd", "2013-1-1"),
                    "Alpha books",
                    "https://bizweb.dktcdn.net/thumb/1024x1024/100/197/269/products/thuat-dam-phan.jpg?v=1552635550060",
                    1
            );
        } catch (ParseException e) {
            System.out.println("Error parsing date");
            return null;
        }
    }

    List<Book> initializeBookList() {
        List<Book> testBookList = new ArrayList<Book>();
        Book testBook1 = initializeTestBook1();
        Book testBook2 = initializeTestBook2();

        testBookList.add(testBook1);
        testBookList.add(testBook2);

        return testBookList;
    }

    @BeforeEach
    void setUp() throws SQLException {
        // Add new Book object
        for (Book book : initializeBookList()) {
            DatabaseManager.addBookToDatabase(book);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Delete book
        for (Book book : initializeBookList()) {
            DatabaseManager.deleteBookFromDatabase(book.getIsbn());
        }
    }

    @Test
    void getAllBooks() throws Exception {
        List<Book> bookList = DatabaseManager.getAllBooks();
        assert bookList != null;
    }

    @Test
    void getBookByISBN() {
        Book testBook = initializeTestBook1();
        // Test added book
        try {
            Book book = DatabaseManager.getBookByISBN("9786043456905");
            assert book != null;
            assertEquals(testBook.getIsbn(), book.getIsbn());
            assertEquals(testBook.getTitle(), book.getTitle());
            assertEquals(testBook.getAuthor(), book.getAuthor());
            assertEquals(testBook.getPublisher(), book.getPublisher());
            assertEquals(testBook.getYearOfPublication(), book.getYearOfPublication());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateBookInDatabase() throws SQLException, ParseException {
        Book testBook2 = initializeTestBook2();
        testBook2.setAvailableAmount(10);

        DatabaseManager.updateBookInDatabase(testBook2);

        Book book = DatabaseManager.getBookByISBN(testBook2.getIsbn());

        assertEquals(testBook2.getAvailableAmount(), book.getAvailableAmount());
    }
}