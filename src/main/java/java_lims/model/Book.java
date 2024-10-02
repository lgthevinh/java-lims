package java_lims.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java_lims.model.DatabaseManager.getConnection;

public class Book {
    private Integer isbn;
    private String title;
    private String author;
    private Date year_of_publication;
    private String publisher;
    private String image_url;

    public static List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Book LIMIT 5");

        while (resultSet.next()) {
            Book book = new Book();
            book.setIsbn(resultSet.getInt("isbn"));
            book.setTitle(resultSet.getString("title"));
            book.setAuthor(resultSet.getString("author"));
            book.setPublisher(resultSet.getString("publisher"));
            book.setYear_of_publication(resultSet.getDate("year_of_publication"));
            book.setImage_url(resultSet.getString("image_url"));

            books.add(book);
        }
        return books;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getYear_of_publication() {
        return year_of_publication;
    }

    public void setYear_of_publication(Date year_of_publication) {
        this.year_of_publication = year_of_publication;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
