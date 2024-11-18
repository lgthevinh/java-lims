package com.lims.dao;

import com.lims.model.Librarian;
import com.lims.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.lims.Utils.convertStringToDatetime;

public class LibrarianDAO extends DatabaseManager {

    public static List<Librarian> getAllLibrarians() throws SQLException, ParseException {
        List<Librarian> librarianList = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Librarian INNER JOIN User ON Librarian.user_id = User.id");
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
                Librarian librarian = new Librarian(user);
                librarian.setEmpId(resultSet.getInt("emp_id"));
                librarianList.add(librarian);
            }
            conn.close();
            return librarianList;
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }
    }

    public static Librarian getLibrarianById(Integer id) throws SQLException, ParseException {
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Librarian INNER JOIN User ON Librarian.user_id = User.id WHERE id = " + id);
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
                conn.close();
                return librarian;
            }
            conn.close();
            return null;
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }
    }

    public static Librarian getLibrarianByEmpId(Integer empId) throws SQLException, ParseException {
        Connection conn = getConnection();
        try {
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
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }

    }

    public static void addLibrarianToDatabase(User user) throws SQLException {
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO Librarian VALUES (null , '%s')".formatted(user.getUserId()));
            conn.close();
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
    }

    public static void deleteLibrarianFromDatabase(Librarian librarian) throws SQLException {
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM Librarian WHERE emp_id = " + librarian.getEmpId());
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
    }

}
