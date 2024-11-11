package org.example;

import com.lims.dao.DatabaseManager;
import com.lims.model.Book;
import com.lims.model.BorrowDetail;
import com.lims.model.User;
import com.lims.controller.AuthenticationController;

import java.text.ParseException;
import java.util.Scanner;
import java.util.List;
import java.util.Date;

import static com.lims.Utils.*;
import static com.lims.dao.DatabaseManager.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to my Application!");
            System.out.println("[1] Log In");
            System.out.println("[2] Log Up");
            System.out.println("[0] Exit");
            System.out.println("Please enter your choice: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                if (login(scanner)) {
                    break;
                }
            } else if (option == 2) {
                register(scanner);
            } else if (option == 0) {
                System.out.println("Exiting the application. Goodbye!");
                return;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        int choice;
        while (true) {
            System.out.println("Welcome to my Application!");
            System.out.println("[1] User Management");
            System.out.println("[2] Document Management");
            System.out.println("[0] Exit");
            System.out.print("Please enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        userManagement(scanner);
                        break;
                    case 2:
                        documentManagement(scanner);
                        break;
                    case 0:
                        System.out.println("Exiting the application. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static boolean login(Scanner scanner) {
        System.out.println("Please log in to continue.");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        AuthenticationController.authenticate(email, password);

        if (AuthenticationController.isAuthenticated()) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Login failed. Please try again.");
            return false;
        }
    }

    private static void register(Scanner scanner) {
        System.out.println("Register a new account.");
        System.out.print("Enter Social ID: ");
        String socialId = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Date of Birth (dd-MM-yyyy): ");
        Date dob = promptForValidDate(scanner);
        System.out.print("Enter Address Line: ");
        String address = scanner.nextLine();
        System.out.print("Enter a valid phone number (10 digits, starting with 0): ");
        String phonenumber;
        while (true) {
            phonenumber = scanner.nextLine();

            if (phonenumber.matches("0\\d{9}")) {
                break;
            } else {
                 System.out.print("Invalid phone number. Please enter a valid phone number: ");
            }
        }
        System.out.print("Enter Email: ");
        String email;
        while (true) {
            email = scanner.nextLine();

            if (email.matches("^[A-Za-z0-9_]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$")) {
                break;
            } else {
                 System.out.print("Email is incorrect. Enter again:");
            }
        }
        String password;
        while (true) {
            System.out.print("Enter Password: ");
            password = scanner.nextLine();

            if (isStrongPassword(password)) {
                break;
            } else {
                System.out.println("Password is weak. Please enter a stronger password.");
            }
        }

        try {
            if (DatabaseManager.getUserBySocialId(socialId) != null) {
                System.out.println("Social ID already exists. Please try again.");
                return;
            }

            if (DatabaseManager.getUserByEmail(socialId) != null) {
                System.out.println("Email already exists. Please try again.");
                return;
            }

            if (DatabaseManager.getUserByPhoneNumber(socialId) != null) {
                System.out.println("Phone Number already exists. Please try again.");
                return;
            }

            User user = new User(socialId, name, dob, address, phonenumber, email, password);
            addUserToDatabase(user);
            AuthenticationController.authenticate(email, password);

            System.out.println("Registration successful! You can now log in.");
        } catch (Exception e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
        }
    }

    private static void userManagement(Scanner scanner) throws Exception {
        int userChoice;
        while (true) {
            System.out.println("User Management Menu:");
            System.out.println("[1] Add User");
            System.out.println("[2] Remove User");
            System.out.println("[3] Update User");
            System.out.println("[4] Display User Info");
            System.out.println("[0] Back to Main Menu");
            System.out.print("Please enter your choice: ");

            userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {
                case 1:
                    addUser(scanner);
                    break;
                case 2:
                    removeUser(scanner);
                    break;
                case 3:
                    updateUser(scanner);
                    break;
                case 4:
                    displayUserInfo(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void documentManagement(Scanner scanner) throws Exception {
        int docChoice;
        while (true) {
            System.out.println("Document Management Menu:");
            System.out.println("[1] Add Document");
            System.out.println("[2] Remove Document");
            System.out.println("[3] Update Document");
            System.out.println("[4] Find Document");
            System.out.println("[5] Display Documents");
            System.out.println("[0] Back to Main Menu");
            System.out.print("Please enter your choice: ");

            docChoice = scanner.nextInt();
            scanner.nextLine();

            switch (docChoice) {
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
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addDocument(Scanner scanner) throws Exception {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        int year = promptForValidYear(scanner);
        Date yearOfPublication = convertStringToDatetime("yyyy", String.valueOf(year));
        System.out.print("Enter Publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter Image URL: ");
        String imageUrl = scanner.nextLine();
        System.out.print("Enter Available Amount: ");
        int availableAmount = scanner.nextInt();
        scanner.nextLine();

        Book book = new Book(isbn, title, author, yearOfPublication, publisher, imageUrl, availableAmount);
        addBook(book);
        System.out.println("Document added successfully.");
    }

    private static void removeDocument(Scanner scanner) throws Exception {
        System.out.print("Enter ISBN of the document to remove:");
        String isbn = scanner.nextLine();
        deleteBook(isbn);
        System.out.println("Document removed successfully.");
    }

    private static void updateDocument(Scanner scanner) throws Exception {
        System.out.print("Enter ISBN of the document to update: ");
        String isbn = scanner.nextLine();
        Book book = getBookByISBN(isbn);

        if (book != null) {
            System.out.println("Current information:");
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Year of Publication: " + formatDatetime("yyyy", book.getYearOfPublication()));
            System.out.println("Publisher: " + book.getPublisher());
            System.out.println("Image URL: " + book.getImageUrl());
            System.out.println("Available Amount: " + book.getAvailableAmount());

            System.out.print("Do you want to update the ISBN? (yes/no): ");
            if(scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.print("Enter new ISBN");
                book.setIsbn(scanner.nextLine());
            }
            System.out.print("Do you want to update the Title? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.print("Enter new Title: ");
                book.setTitle(scanner.nextLine());
            }

            System.out.print("Do you want to update the Author? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.print("Enter new Author: ");
                book.setAuthor(scanner.nextLine());
            }

            System.out.print("Do you want to update the Year of Publication? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.print("Enter new Year of Publication (yyyy): ");
                String dateString = scanner.nextLine();
                Date year = convertStringToDatetime("yyyy", dateString);
                book.setYearOfPublication(year);
            }

            System.out.print("Do you want to update the Publisher? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.print("Enter new Publisher: ");
                book.setPublisher(scanner.nextLine());
            }

            System.out.print("Do you want to update the Image URL? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.print("Enter new Image URL: ");
                book.setImageUrl(scanner.nextLine());
            }

            System.out.print("Do you want to update the Available Amount? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.print("Enter new Available Amount: ");
                book.setAvailableAmount(scanner.nextInt());
                scanner.nextLine();
            }

            updateBook(book);
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
        System.out.print("Enter Social ID:");
        String socialId = scanner.nextLine();
        System.out.print("Enter Name:");
        String name = scanner.nextLine();
        System.out.print("Enter Date of Birth (dd-MM-yyyy):");
        Date dob = promptForValidDate(scanner);
        System.out.print("Enter Address Line:");
        String address = scanner.nextLine();
        System.out.print("Enter Phone Number:");
        String phone = scanner.nextLine();
        System.out.print("Enter Email:");
        String email = scanner.nextLine();
        System.out.print("Enter Password:");
        String password = scanner.nextLine();

        User user = new User(socialId, name, dob, address, phone, email, password);
        addUserToDatabase(user);
        System.out.println("User added successfully.");
    }

    private static void removeUser(Scanner scanner) throws Exception {
        System.out.print("Enter email of User to remove:");
        String email = scanner.nextLine();
        User user = getUserByEmail(email);
        deleteUserFromDatabase(user);
        System.out.println("Remove user successfully.");
    }

    private static void updateUser(Scanner scanner) throws Exception{
        System.out.print("Enter the email of user to update");
        String email = scanner.nextLine();
        User user = getUserByEmail(email);
        if(user != null) {
            System.out.print("Enter new ");
        }
    }

    private static void borrowDocument(Scanner scanner) {
        try {
            System.out.print("Enter the ISBN of the book to borrow:");
            String bookIsbn = scanner.nextLine();
            System.out.print("Enter the borrower ID:");
            int borrowerId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter the librarian ID:");
            int librarianId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter the borrow date (dd-MM-yyyy):");
            Date borrowDate = promptForValidDate(scanner);
            System.out.print("Enter the expected return date (dd-MM-yyyy):");
            Date expectedReturnDate = promptForValidDate(scanner);

            BorrowDetail borrowDetail = new BorrowDetail(
                    bookIsbn,
                    borrowerId,
                    librarianId,
                    borrowDate,
                    expectedReturnDate,
                    null
            );

            addBorrowDetailToDatabase(borrowDetail);
            System.out.println("The document has been successfully borrowed.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void returnDocument(Scanner scanner) {
        try {
            System.out.print("Enter the borrow detail ID to return:");
            int borrowDetailId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter the actual return date (dd-MM-yyyy):");
            Date actualReturnDateStr = promptForValidDate(scanner);

            BorrowDetail borrowDetail = getBorrowDetailById(borrowDetailId);
            if (borrowDetail != null) {
                borrowDetail.setActualReturnDate(actualReturnDateStr);
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
        System.out.print("Enter User ID to display info:");
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