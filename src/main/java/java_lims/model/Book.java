package java_lims.model;

import java.util.Date;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private Date yearOfPublication;
    private String publisher;
    private String imageUrl;
    private Integer availableAmount;

    public Book(String isbn, String title, String author, Date yearOfPublication, String publisher, String imageUrl, Integer availableAmount) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
        this.availableAmount = availableAmount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
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

    public Date getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Date year_of_publication) {
        this.yearOfPublication = year_of_publication;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image_url) {
        this.imageUrl = image_url;
    }

    public Integer getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Integer availableAmount) {
        this.availableAmount = availableAmount;
    }
}
