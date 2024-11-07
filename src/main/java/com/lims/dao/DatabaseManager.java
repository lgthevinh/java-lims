package com.lims.dao;

import com.lims.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:./src/main/resources/database/db.sqlite";

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static List<Book> getAllBooks() throws SQLException {
        return BookDAO.getAllBooks();
    }

    public static Book getBookByISBN(String isbn) throws SQLException {
        return BookDAO.getBookByISBN(isbn);
    }

    public static void addBookToDatabase(Book book) throws SQLException {
        BookDAO.addBookToDatabase(book);
    }

    public static void deleteBookFromDatabase(String isbn) throws SQLException {
        BookDAO.deleteBookFromDatabase(isbn);
    }

    public static void updateBookInDatabase(Book book) throws SQLException {
        BookDAO.updateBookInDatabase(book);
    }

    public static List<User> getAllUsers() throws SQLException, ParseException {
        return UserDAO.getAllUsers();
    }

    public static User getUserById(Integer id) throws SQLException, ParseException {
        return UserDAO.getUserById(id);
    }

    public static User getUserByEmail(String email) throws SQLException, ParseException {
        return UserDAO.getUserByEmail(email);
    }

    public static void addUserToDatabase(User user) throws SQLException {
        UserDAO.addUserToDatabase(user);
    }

    public static void deleteUserFromDatabase(User user) throws SQLException {
        UserDAO.deleteUserFromDatabase(user);
    }

    public static void updateUserInDatabase(User user) throws SQLException {
        UserDAO.updateUserInDatabase(user);
    }

    public static List<Librarian> getAllLibrarians() throws SQLException, ParseException {
        return LibrarianDAO.getAllLibrarians();
    }

    public static Librarian getLibrarianById(Integer id) throws SQLException, ParseException {
        return LibrarianDAO.getLibrarianById(id);
    }

    public static Librarian getLibrarianByEmpId(Integer empId) throws SQLException, ParseException {
        return LibrarianDAO.getLibrarianByEmpId(empId);
    }

    public static void addLibrarianToDatabase(User user) throws SQLException {
        LibrarianDAO.addLibrarianToDatabase(user);
    }

    public static void addLibrarianToDatabase(Librarian librarian) throws SQLException {
        LibrarianDAO.addLibrarianToDatabase(librarian);
    }

    public static void deleteLibrarianFromDatabase(Librarian librarian) throws SQLException {
        LibrarianDAO.deleteLibrarianFromDatabase(librarian);
    }

    public static List<Student> getAllStudents() throws SQLException, ParseException {
        return StudentDAO.getAllStudents();
    }

    public static Student getStudentById(Integer id) throws SQLException, ParseException {
        return StudentDAO.getStudentById(id);
    }

    public static Student getStudentByStudentId(String studentId) throws SQLException, ParseException {
        return StudentDAO.getStudentByStudentId(studentId);
    }

    public static void addStudentToDatabase(Student student) throws SQLException {
        StudentDAO.addStudentToDatabase(student);
    }

    public static void addStudentToDatabase(User user, String studentId, String school, String major) throws SQLException {
        StudentDAO.addStudentToDatabase(user, studentId, school, major);
    }

    public static void deleteStudentFromDatabase(Student student) throws SQLException {
        StudentDAO.deleteStudentFromDatabase(student);
    }

    public static void updateStudentInDatabase(Student student) throws SQLException {
        StudentDAO.updateStudentInDatabase(student);
    }

    public static List<BorrowDetail> getAllBorrowDetail() throws SQLException, ParseException {
        return BorrowDetailDAO.getAllBorrowDetail();
    }

    public static BorrowDetail getBorrowDetailById(Integer id) throws SQLException, ParseException {
        return BorrowDetailDAO.getBorrowDetailById(id);
    }

    public static void addBorrowDetailToDatabase(BorrowDetail borrowDetail) throws SQLException {
        BorrowDetailDAO.addBorrowDetailToDatabase(borrowDetail);
    }

    public static void deleteBorrowDetailFromDatabase(BorrowDetail borrowDetail) throws SQLException {
        BorrowDetailDAO.deleteBorrowDetailFromDatabase(borrowDetail);
    }

    public static void updateBorrowDetailInDatabase(BorrowDetail borrowDetail) throws SQLException {
        BorrowDetailDAO.updateBorrowDetailInDatabase(borrowDetail);
    }
}