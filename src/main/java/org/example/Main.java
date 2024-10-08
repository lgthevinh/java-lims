package org.example;

import com.lims.model.Book;

import java.util.List;

import static com.lims.Utils.convertStringToDatetime;
import static com.lims.dao.DatabaseManager.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Book book1 = new Book("9780385490818", "The House of the Spirits", "Isabel Allende", convertStringToDatetime("yyyy-MM-dd", "1982-10-01"), "Knopf", "https://images.com/house_spirits.jpg", 3);
        Book book2 = new Book("9780670020553", "The Girl on the Train", "Paula Hawkins", convertStringToDatetime("yyyy-MM-dd", "2015-01-13"), "Riverhead", "https://images.com/girl_on_train.jpg", 5);

        addBookToDatabase(book1);
        addBookToDatabase(book2);

        try {
            List<Book> bookList = getAllBooks();
            Book findBook = getBookByISBN("9780670020553");

            System.out.println(bookList.getFirst().getTitle().equals("The House of the Spirits"));
            System.out.println(findBook != null ? findBook.getTitle() : null);
        } catch (Exception e) {
            System.out.println(e);
        }

        deleteBookFromDatabase(book1.getIsbn());
        deleteBookFromDatabase(book2.getIsbn());

        System.out.println("List size: " + getAllBooks().size());
    }
}