package com.lims.dao;

import com.lims.model.Student;
import com.lims.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.lims.Utils.convertStringToDatetime;

public class StudentDAO extends DatabaseManager {

    public static List<Student> getAllStudents() throws SQLException, ParseException {
        List<Student> studentList = new ArrayList<>();
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Student INNER JOIN User ON Student.user_id = User.id");
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

    public static Student getStudentById(Integer userId) throws SQLException, ParseException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Student INNER JOIN User ON Student.user_id = User.id WHERE social_id= '%s'".formatted(userId));
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

    public static Student getStudentByStudentId(String studentId) throws SQLException, ParseException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Student INNER JOIN User ON Student.user_id = User.id WHERE student_id = '%s'".formatted(studentId));
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

    public static void addStudentToDatabase(User user, String studentId, String school, String major) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO Student VALUES ('%s', %d, '%s', %s)".formatted(
                studentId,
                user.getUserId(),
                school,
                major
        ));
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

}
