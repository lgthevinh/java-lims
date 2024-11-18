package com.lims.dao;

import com.lims.model.BorrowDetail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.lims.Utils.convertStringToDatetime;
import static com.lims.Utils.formatDatetime;

public class BorrowDetailDAO extends DatabaseManager {

    public static List<BorrowDetail> getAllBorrowDetail() throws SQLException, ParseException {
        List<BorrowDetail> borrowDetailList = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM BorrowDetail");
            while (resultSet.next()) {
                BorrowDetail borrowDetail = new BorrowDetail(
                        resultSet.getString("book_isbn"),
                        resultSet.getInt("borrower_id"),
                        resultSet.getInt("librarian_id"),
                        convertStringToDatetime("MM-dd-yyyy", resultSet.getString("borrow_date")),
                        convertStringToDatetime("MM-dd-yyyy", resultSet.getString("expected_return_date")),
                        convertStringToDatetime("MM-dd-yyyy", resultSet.getString("actual_return_date"))
                );
                borrowDetailList.add(borrowDetail);
            }
            conn.close();
            return borrowDetailList;
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }
    }

    public static BorrowDetail getBorrowDetailById(Integer id) throws SQLException, ParseException {
        Connection conn = getConnection();
        try {
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
        } catch (SQLException | ParseException e) {
            conn.close();
            throw e;
        }
    }

    public static void addBorrowDetailToDatabase(BorrowDetail borrowDetail) throws SQLException {
        Connection conn = getConnection();
        try {
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
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
    }

    public static void deleteBorrowDetailFromDatabase(BorrowDetail borrowDetail) throws SQLException {
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM BorrowDetail WHERE id = " + borrowDetail.getId());
            conn.close();
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
    }

    public static void updateBorrowDetailInDatabase(BorrowDetail borrowDetail) throws SQLException {
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE BorrowDetail SET book_isbn = '%s', borrower_id = %d, librarian_id = %d, borrow_date = '%s', expected_return_date = '%s', actual_return_date = '%s' WHERE id = %d".formatted(
                    borrowDetail.getBookIsbn(),
                    borrowDetail.getBorrowerId(),
                    borrowDetail.getLibrarianId(),
                    formatDatetime("MM-dd-yyyy", borrowDetail.getBorrowDate()),
                    formatDatetime("MM-dd-yyyy", borrowDetail.getExpectedReturnDate()),
                    formatDatetime("MM-dd-yyyy", borrowDetail.getActualReturnDate()),
                    borrowDetail.getId()
            ));
            conn.close();
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
    }

}
