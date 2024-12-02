package com.lims.testcase;

import com.lims.Utils;
import com.lims.dao.UserDAO;
import com.lims.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    // Khởi tạo đối tượng User mẫu
    User initializeTestUser1() {
        try {
            return new User(
                    "111111111",
                    "Nguyen Van A",
                    Utils.convertStringToDatetime("MM-dd-yyyy", "01-01-1990"),
                    "123 ABC Street",
                    "09999999999",
                    "nguyenvana@example.com",
                    "password123"
            );
        } catch (ParseException e) {
            System.out.println("Error parsing date");
            return null;
        }
    }

    User initializeTestUser2() {
        try {
            return new User(
                    "222222222",
                    "Tran Thi B",
                    Utils.convertStringToDatetime("MM-dd-yyyy", "05-15-1985"),
                    "456 DEF Street",
                    "01111111111",
                    "tranthib@example.com",
                    "securepassword"
            );
        } catch (ParseException e) {
            System.out.println("Error parsing date");
            return null;
        }
    }

    List<User> initializeUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(initializeTestUser1());
        userList.add(initializeTestUser2());
        return userList;
    }

    @BeforeEach
    void setUp() throws SQLException {
        // Thêm User mẫu vào cơ sở dữ liệu
        for (User user : initializeUserList()) {
            UserDAO.addUserToDatabase(user);
        }
    }

    @AfterEach
    void tearDown() throws SQLException, ParseException {
        // Xóa User mẫu khỏi cơ sở dữ liệu
        for (User user : UserDAO.getAllUsers()) {
            UserDAO.deleteUserFromDatabase(user);
        }
    }

    @Test
    void getAllUsers() throws Exception {
        List<User> userList = UserDAO.getAllUsers();
        assertNotNull(userList);
        assertEquals(2, userList.size());
    }

    @Test
    void getUserById() throws Exception {
        List<User> userList = UserDAO.getAllUsers();
        User testUser = userList.get(0);
        User user = UserDAO.getUserById(testUser.getUserId());

        assertNotNull(user);
        assertEquals(testUser.getSocialId(), user.getSocialId());
        assertEquals(testUser.getName(), user.getName());
    }

    @Test
    void getUserBySocialId() throws Exception {
        User testUser = initializeTestUser1();
        User user = UserDAO.getUserBySocialId(testUser.getSocialId());

        assertNotNull(user);
        assertEquals(testUser.getSocialId(), user.getSocialId());
        assertEquals(testUser.getName(), user.getName());
    }

    @Test
    void getUserByEmail() throws Exception {
        User testUser = initializeTestUser2();
        User user = UserDAO.getUserByEmail(testUser.getEmail());

        assertNotNull(user);
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getName(), user.getName());
    }

    @Test
    void updateUserInDatabase() throws SQLException, ParseException {
        List<User> userList = UserDAO.getAllUsers();
        User testUser = userList.get(0);
        testUser.setAddressLine("New Updated Address");

        UserDAO.updateUserInDatabase(testUser);

        User updatedUser = UserDAO.getUserById(testUser.getUserId());
        assertNotNull(updatedUser);
        assertEquals("New Updated Address", updatedUser.getAddressLine());
    }
}
