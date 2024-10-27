package org.example;

import com.lims.model.Book;
import com.lims.model.BorrowDetail;
import com.lims.model.User;
import java.util.Scanner;
import java.util.List;
import java.util.Date;

import static com.lims.Utils.convertStringToDatetime;
import static com.lims.dao.DatabaseManager.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("Welcome to My Application!");
            System.out.println("[0] Exit");
            System.out.println("[1] Add Document");
            System.out.println("[2] Remove Document");
            System.out.println("[3] Update Document");
            System.out.println("[4] Find Document");
            System.out.println("[5] Display Document");
            System.out.println("[6] Add User");
            System.out.println("[7] Borrow Document");
            System.out.println("[8] Return Document");
            System.out.println("[9] Display User Info");
            System.out.print("Please enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Action is not supported");
                scanner.next();
                continue;
            }

            try {
                switch (choice) {
                    case 0:
                        System.out.println("Exiting the application. Goodbye!");
                        scanner.close();
                        return;
                    case 1:
                        addDocument(scanner);
                        break;
                    case 2:
                        removeDocument(scanner);
                        break;
                    case 3:
                        updateDocument(scanner);
                        break;
                    case 4:
                        findDocument(scanner);
                        break;
                    case 5:
                        displayDocuments();
                        break;
                    case 6:
                        addUser(scanner);
                        break;
                    case 7:
                        borrowDocument(scanner);
                        break;
                    case 8:
                        returnDocument(scanner);
                        break;
                    case 9:
                        displayUserInfo(scanner);
                        break;
                    default:
                        System.out.println("Action is not supported");
                        break;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void addDocument(Scanner scanner) throws Exception {
        System.out.println("Enter ISBN:");
        String isbn = scanner.nextLine();
        System.out.println("Enter Title:");
        String title = scanner.nextLine();
        System.out.println("Enter Author:");
        String author = scanner.nextLine();
        System.out.println("Enter Year of Publication (yyyy):");
        String dateString = scanner.nextLine();
        String dateFormat = "yyyy-MM-dd";
        Date year = convertStringToDatetime(dateFormat, dateString);
        System.out.println("Enter Publisher:");
        String publisher = scanner.nextLine();
        System.out.println("Enter Image URL:");
        String imageUrl = scanner.nextLine();
        System.out.println("Enter Available Amount:");
        int availableAmount = scanner.nextInt();
        scanner.nextLine();

        Book book = new Book(isbn, title, author, year, publisher, imageUrl, availableAmount);
        addBookToDatabase(book);
        System.out.println("Document added successfully.");
    }

    private static void removeDocument(Scanner scanner) throws Exception {
        System.out.println("Enter ISBN of the document to remove:");
        String isbn = scanner.nextLine();
        deleteBookFromDatabase(isbn);
        System.out.println("Document removed successfully.");
    }

    private static void updateDocument(Scanner scanner) throws Exception {
        System.out.println("Enter ISBN of the document to update:");
        String isbn = scanner.nextLine();
        Book book = getBookByISBN(isbn);
        if (book != null) {
            System.out.println("Enter new Title:");
            book.setTitle(scanner.nextLine());
            System.out.println("Enter new Author:");
            book.setAuthor(scanner.nextLine());
            System.out.println("Enter new Year of Publication (yyyy):");
            String dateString = scanner.nextLine();
            String dateFormat = "yyyy-MM-dd";
            Date year = convertStringToDatetime(dateFormat, dateString);
            book.setYearOfPublication(year);
            System.out.println("Enter new Publisher:");
            book.setPublisher(scanner.nextLine());
            System.out.println("Enter new Image URL:");
            book.setImageUrl(scanner.nextLine());
            System.out.println("Enter new Available Amount:");
            book.setAvailableAmount(scanner.nextInt());
            scanner.nextLine();

            updateBookInDatabase(book);
            System.out.println("Document updated successfully.");
        } else {
            System.out.println("Document not found.");
        }
    }

    private static void findDocument(Scanner scanner) throws Exception {
        System.out.println("Enter ISBN of the document to find:");
        String isbn = scanner.nextLine();
        Book book = getBookByISBN(isbn);
        if (book != null) {
            System.out.println("Document found: " + book);
        } else {
            System.out.println("Document not found.");
        }
    }

    private static void displayDocuments() throws Exception {
        List<Book> books = getAllBooks();
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void addUser(Scanner scanner) throws Exception {
        System.out.println("Enter Social ID:");
        String socialId = scanner.nextLine();
        System.out.println("Enter Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Date of Birth (yyyy-MM-dd):");
        String dateString = scanner.nextLine();
        String dateFormat = "yyyy-MM-dd";
        Date dob = convertStringToDatetime(dateFormat, dateString);
        System.out.println("Enter Address Line:");
        String address = scanner.nextLine();
        System.out.println("Enter Phone Number:");
        String phone = scanner.nextLine();
        System.out.println("Enter Email:");
        String email = scanner.nextLine();
        System.out.println("Enter Password:");
        String password = scanner.nextLine();

        User user = new User(socialId, name, dob, address, phone, email, password);
        addUserToDatabase(user);
        System.out.println("User added successfully.");
    }

    private static void borrowDocument(Scanner scanner) {
        try {
            System.out.println("Enter the ISBN of the book to borrow:");
            String bookIsbn = scanner.nextLine();
            System.out.println("Enter the borrower ID:");
            int borrowerId = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the librarian ID:");
            int librarianId = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the borrow date (yyyy-MM-dd):");
            String borrowDateStr = scanner.nextLine();
            System.out.println("Enter the expected return date (yyyy-MM-dd):");
            String expectedReturnDateStr = scanner.nextLine();

            BorrowDetail borrowDetail = new BorrowDetail(
                    bookIsbn,
                    borrowerId,
                    librarianId,
                    convertStringToDatetime("yyyy-MM-dd", borrowDateStr),
                    convertStringToDatetime("yyyy-MM-dd", expectedReturnDateStr),
                    null // Actual return date is not yet available
            );

            addBorrowDetailToDatabase(borrowDetail);
            System.out.println("The document has been successfully borrowed.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void returnDocument(Scanner scanner) {
        try {
            System.out.println("Enter the borrow detail ID to return:");
            int borrowDetailId = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the actual return date (yyyy-MM-dd):");
            String actualReturnDateStr = scanner.nextLine();

            BorrowDetail borrowDetail = getBorrowDetailById(borrowDetailId);
            if (borrowDetail != null) {
                borrowDetail.setActualReturnDate(convertStringToDatetime("yyyy-MM-dd", actualReturnDateStr));
                updateBorrowDetailInDatabase(borrowDetail);
                System.out.println("The document has been successfully returned.");
            } else {
                System.out.println("No borrow detail found with the entered ID.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void displayUserInfo(Scanner scanner) throws Exception {
        System.out.println("Enter User ID to display info:");
        int userId = scanner.nextInt();
        scanner.nextLine();
        User user = getUserById(userId);
        if (user != null) {
            System.out.println("User Info: " + user);
        } else {
            System.out.println("User not found.");
        }
    }
}