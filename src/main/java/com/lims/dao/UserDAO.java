package com.lims.dao;

import com.lims.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.lims.Utils.convertStringToDatetime;
import static com.lims.Utils.formatDatetime;

public class UserDAO extends DatabaseManager {

    public static List<User> getAllUsers() throws SQLException, ParseException {
        List<User> userList = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM User");

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("social_id"),
                    resultSet.getString("name"),
                    convertStringToDatetime("yyyy-MM-dd", resultSet.getString("date_of_birth")),
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
                    convertStringToDatetime("yyyy-MM-dd", resultSet.getString("date_of_birth")),
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
                formatDatetime("yyyy-MM-dd", user.getDateOfBirth()),
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
                formatDatetime("yyyy-MM-dd", user.getDateOfBirth()),
                user.getAddressLine(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getUserId()
        ));
        conn.close();
    }

}
