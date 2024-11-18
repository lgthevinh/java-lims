package com.lims.dao;

import com.lims.model.User;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.lims.Utils.convertStringToDatetime;
import static com.lims.Utils.formatDatetime;

public class UserDAO extends DatabaseManager {

    public static List<User> getAllUsers() throws SQLException, ParseException {
        List<User> userList = new ArrayList<>();
        Connection conn = getConnection();
        try {
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
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }
    }

    public static User getUserById(Integer id) throws SQLException, ParseException {
        Connection conn = getConnection();
        try {
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
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }

    }

    public static User getUserByEmail(String email) throws SQLException, ParseException {
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM User WHERE email = '%s'".formatted(email));

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
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }
    }

    public static void addUserToDatabase(User user) throws SQLException {
        Connection conn = getConnection();
        try {
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
        } catch (SQLException e) {
            conn.close();
            throw e;
        }

    }

    public static void deleteUserFromDatabase(User user) throws SQLException {
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM User WHERE id = " + user.getUserId());
            conn.close();
        } catch (SQLException e) {
            conn.close();
            throw e;
        }

    }

    public static void updateUserInDatabase(User user) throws SQLException {
        Connection conn = getConnection();
        try {
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
        } catch (SQLException e) {
            conn.close();
            throw e;
        }

    }

    public static User getUserBySocialId(String socialId) throws SQLException, ParseException {
        Connection conn = getConnection();
        try {
            String query = "SELECT * FROM User WHERE social_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, socialId);  // Chuyển socialId thành chuỗi
            ResultSet resultSet = preparedStatement.executeQuery();

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
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }

    }

}
