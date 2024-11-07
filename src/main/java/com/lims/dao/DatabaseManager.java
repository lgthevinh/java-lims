package com.lims.dao;

import com.lims.model.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.lims.Utils.convertStringToDatetime;
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
            String dateString = resultSet.getString("year_of_publication");
            Date yearOfPublication = null;
            try {
                yearOfPublication = new SimpleDateFormat("MM-dd-yyyy").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Book book = new Book(
                    resultSet.getString("isbn"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    yearOfPublication,
                    resultSet.getString("publisher"),
                    resultSet.getString("image_url"),
                    resultSet.getInt("available_amount")
            );
            books.add(book);
        }
        conn.close();
        return books;
    }

    public static Book getBookByISBN(String isbn) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Book WHERE isbn = " + isbn);

        if (resultSet.next()) {
            Book book = new Book(
                    resultSet.getString("isbn"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getDate("year_of_publication"),
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
        String sqlStatement = "INSERT INTO Book VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d)".formatted(
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                formatDatetime("MM-dd-yyyy", book.getYearOfPublication()),
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

    public static List<User> getAllUsers() throws SQLException, ParseException {
        List<User> userList = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM User");

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("social_id"),
                    resultSet.getString("name"),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("date_of_birth")),
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

    public static User getUserById(Integer id) throws SQLException, ParseException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM User WHERE id = " + id);

        if (resultSet.next()) {
            User user = new User(
                    resultSet.getString("social_id"),
                    resultSet.getString("name"),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("date_of_birth")),
                    resultSet.getString("address_line"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
            user.setUserId(resultSet.getInt("id"));
            conn.close();
            return user;
        }

        conn.close();
        return null;
    }

    public static void addUserToDatabase(User user) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        String sqlStatement = "INSERT INTO USER VALUES (null ,'%s', '%s', '%s', '%s', '%s', '%s', '%s')".formatted(
                user.getSocialId(),
                user.getName(),
                formatDatetime("MM-dd-yyyy", user.getDateOfBirth()),
                user.getAddressLine(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword()
        );
        statement.executeUpdate(sqlStatement);
        conn.close();
    }

    public static void deleteUserFromDatabase(User user) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("DELETE FROM User WHERE id = " + user.getUserId());
        conn.close();
    }

    public static void updateUserInDatabase(User user) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("UPDATE User SET social_id = '%s', name = '%s', date_of_birth = '%s', address_line = '%s', phone_number = '%s', email = '%s', password = '%s' WHERE id = %d".formatted(
                user.getSocialId(),
                user.getName(),
                formatDatetime("MM-dd-yyyy", user.getDateOfBirth()),
                user.getAddressLine(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getUserId()
        ));
        conn.close();
    }

    public static List<Librarian> getAllLibrarian() throws SQLException, ParseException {
        List<Librarian> librarianList = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Librarian INNER JOIN User ON Librarian.user_id = User.id");
        while (resultSet.next()) {
            String dateString = resultSet.getString("date_of_birth");
            Date dateOfBirth = null;
            try {
                dateOfBirth = new SimpleDateFormat("MM-dd-yyyy").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            User user = new User(
                    resultSet.getString("social_id"),
                    resultSet.getString("name"),
                    dateOfBirth,
                    resultSet.getString("address_line"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
            Librarian librarian = new Librarian(user);
            librarian.setEmpId(resultSet.getInt("emp_id"));
            librarianList.add(librarian);
        }
        conn.close();
        return librarianList;
    }

    public static Librarian getLibrarianByEmpId(Integer empId) throws SQLException, ParseException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Librarian INNER JOIN User ON Librarian.user_id = User.id WHERE emp_id = " + empId);
        if (resultSet.next()) {
            User user = new User(
                    resultSet.getString("social_id"),
                    resultSet.getString("name"),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("date_of_birth")),
                    resultSet.getString("address_line"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
            Librarian librarian = new Librarian(user);
            librarian.setEmpId(resultSet.getInt("emp_id"));
            conn.close();
            return librarian;
        }
        conn.close();
        return null;
    }

    public static void addLibrarianToDatabase(Librarian librarian) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO Librarian VALUES (null , '%s')".formatted(librarian.getUserId()));
        conn.close();
    }

    public static void deleteLibrarianFromDatabase(Librarian librarian) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("DELETE FROM Librarian WHERE emp_id = " + librarian.getEmpId());
    }

    public static List<Student> getAllStudents() throws SQLException, ParseException {
        List<Student> studentList = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Student");
        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("social_id"),
                    resultSet.getString("name"),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("date_of_birth")),
                    resultSet.getString("address_line"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
            Student student = new Student(user);
            student.setStudentId(resultSet.getString("student_id"));
            student.setSchool(resultSet.getString("school_name"));
            student.setMajor(resultSet.getString("major"));
            studentList.add(student);
        }
        conn.close();
        return studentList;
    }

    public static Student getStudentByStudentId(String student_id) throws SQLException, ParseException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Student INNER JOIN User ON Student.user_id = User.id WHERE student_id = '%s'".formatted(student_id));
        if (resultSet.next()) {
            User user = new User(
                    resultSet.getString("social_id"),
                    resultSet.getString("name"),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("date_of_birth")),
                    resultSet.getString("address_line"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
            Student student = new Student(user);
            student.setStudentId(resultSet.getString("student_id"));
            student.setSchool(resultSet.getString("school_name"));
            student.setMajor(resultSet.getString("major"));
            conn.close();
            return student;
        }
        conn.close();
        return null;
    }

    public static void addStudentToDatabase(Student student) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO Student VALUES ('%s', %d, '%s', %s)".formatted(
                student.getStudentId(),
                student.getUserId(),
                student.getSchool(),
                student.getMajor()
        ));
        conn.close();
    }

    public static void deleteStudentFromDatabase(Student student) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("DELETE FROM Student where student_id = '%s'".formatted(student.getStudentId()));
        conn.close();
    }

    public static void updateStudentInDatabase(Student student) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("UPDATE Student SET student_id = '%s', school_name = '%s', major = '%s' WHERE user_id = %d".formatted(
                student.getStudentId(),
                student.getSchool(),
                student.getMajor(),
                student.getUserId()
        ));
        conn.close();
    }

    public static List<BorrowDetail> getAllBorrowDetail() throws SQLException {
        List<BorrowDetail> borrowDetailList = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM BorrowDetail");
        return borrowDetailList;
    }

    public static BorrowDetail getBorrowDetailById(Integer id) throws SQLException, ParseException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM BorrowDetail WHERE id = " + id);
        if (resultSet.next()) {
            BorrowDetail borrowDetail = new BorrowDetail(
                    resultSet.getString("book_isbn"),
                    resultSet.getInt("borrower_id"),
                    resultSet.getInt("librarian_id"),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("borrow_date")),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("expected_return_date")),
                    convertStringToDatetime("MM-dd-yyyy", resultSet.getString("actual_return_date"))
            );
            conn.close();
            return borrowDetail;
        }
        conn.close();
        return null;
    }

    public static void addBorrowDetailToDatabase(BorrowDetail borrowDetail) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO BorrowDetail VALUES (null, '%s', %d, %d, '%s', '%s', '%s')".formatted(
                borrowDetail.getBookIsbn(),
                borrowDetail.getBorrowerId(),
                borrowDetail.getLibrarianId(),
                formatDatetime("MM-dd-yyyy", borrowDetail.getBorrowDate()),
                formatDatetime("MM-dd-yyyy", borrowDetail.getExpectedReturnDate()),
                formatDatetime("MM-dd-yyyy", borrowDetail.getActualReturnDate())
        ));
        conn.close();
    }

    public static void deleteBorrowDetailFromDatabase(BorrowDetail borrowDetail) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("DELETE FROM BorrowDetail WHERE id = " + borrowDetail.getId());
        conn.close();
    }

    public static void updateBorrowDetailInDatabase(BorrowDetail borrowDetail) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("UPDATE BorrowDetail SET book_isbn = '%s', borrower_id = %d, librarian_id = %d, borrow_date = '%s', expected_return_date = '%s', actual_return_date = '%s' WHERE id = %d".formatted(
                borrowDetail.getBookIsbn(),
                borrowDetail.getBorrowerId(),
                borrowDetail.getLibrarianId(),
                borrowDetail.getBorrowDate(),
                borrowDetail.getExpectedReturnDate(),
                borrowDetail.getActualReturnDate(),
                borrowDetail.getId()
        ));
        conn.close();
    }
}
