package org.example;

import com.lims.model.Book;
import com.lims.model.User;

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

        User user1 = new User("031205002163", "Luong The Vinh", convertStringToDatetime("yyyy-MM-dd", "2005-08-03"), "S3.03 Vinhomes Smart City, Nam Tu Liem, Hanoi", "0398742752", "everwellmax@gmail.com", "test@123");
        User user2 = new User("031001102552", "Nguyen Van A", convertStringToDatetime("yyyy-MM-dd", "2004-02-03"), "Nam Tu Liem, Hanoi", "034325345", "nguyenvana@gmail.com", "test@123");

        addUserToDatabase(user1);
        addUserToDatabase(user2);

        List<User> userList = getAllUsers();
        System.out.println(userList.get(1).getName());

        user1.setAddressLine("Nam Tu Liem, Hanoi");
        updateUserInDatabase(user1);

        System.out.println(getUserById(user1.getUserId()).getAddressLine());

        deleteUserFromDatabase(String.valueOf(user1.getUserId()));
        deleteUserFromDatabase(String.valueOf(user2.getUserId()));
        System.out.println("User list size: " + getAllUsers().size());
    }
}