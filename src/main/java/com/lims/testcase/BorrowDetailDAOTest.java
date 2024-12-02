package com.lims.testcase;

import com.lims.Utils;
import com.lims.dao.BorrowDetailDAO;
import com.lims.model.BorrowDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BorrowDetailDAOTest {

    BorrowDetail initializeTestBorrowDetail1() {
        try {
            return new BorrowDetail(
                    "9786043456905",
                    1,
                    101,
                    Utils.convertStringToDatetime("MM-dd-yyyy", "12-01-2023"),
                    Utils.convertStringToDatetime("MM-dd-yyyy", "12-15-2023"),
                    null
            );
        } catch (ParseException e) {
            System.out.println("Error parsing date");
            return null;
        }
    }

    BorrowDetail initializeTestBorrowDetail2() {
        try {
            return new BorrowDetail(
                    "9786043652307",
                    2,
                    102,
                    Utils.convertStringToDatetime("MM-dd-yyyy", "11-01-2023"),
                    Utils.convertStringToDatetime("MM-dd-yyyy", "11-15-2023"),
                    Utils.convertStringToDatetime("MM-dd-yyyy", "11-10-2023")
            );
        } catch (ParseException e) {
            System.out.println("Error parsing date");
            return null;
        }
    }

    List<BorrowDetail> initializeBorrowDetailList() {
        List<BorrowDetail> borrowDetailList = new ArrayList<>();
        borrowDetailList.add(initializeTestBorrowDetail1());
        borrowDetailList.add(initializeTestBorrowDetail2());
        return borrowDetailList;
    }

    @BeforeEach
    void setUp() throws SQLException {
        // Add test BorrowDetails to the database
        for (BorrowDetail borrowDetail : initializeBorrowDetailList()) {
            BorrowDetailDAO.addBorrowDetailToDatabase(borrowDetail);
        }
    }

    @AfterEach
    void tearDown() throws SQLException, ParseException {
        // Delete test BorrowDetails from the database
        for (BorrowDetail borrowDetail : BorrowDetailDAO.getAllBorrowDetail()) {
            BorrowDetailDAO.deleteBorrowDetailFromDatabase(borrowDetail);
        }
    }

    @Test
    void getAllBorrowDetails() throws Exception {
        List<BorrowDetail> borrowDetailList = BorrowDetailDAO.getAllBorrowDetail();
        assertNotNull(borrowDetailList);
        assertEquals(2, borrowDetailList.size());
    }

    @Test
    void updateBorrowDetailInDatabase() throws SQLException, ParseException {
        BorrowDetail testBorrowDetail = initializeTestBorrowDetail2();
        List<BorrowDetail> borrowDetailList = BorrowDetailDAO.getAllBorrowDetail();
        BorrowDetail toUpdate = borrowDetailList.get(1);
        toUpdate.setActualReturnDate(Utils.convertStringToDatetime("MM-dd-yyyy", "12-20-2023"));

        BorrowDetailDAO.updateBorrowDetailInDatabase(toUpdate);
        BorrowDetail updatedBorrowDetail = BorrowDetailDAO.getBorrowDetailById(toUpdate.getId());

        assertNotNull(updatedBorrowDetail);
        assertEquals(toUpdate.getActualReturnDate(), updatedBorrowDetail.getActualReturnDate());
    }

    @Test
    void deleteBorrowDetailFromDatabase() throws SQLException, ParseException {
        List<BorrowDetail> borrowDetailList = BorrowDetailDAO.getAllBorrowDetail();
        BorrowDetail toDelete = borrowDetailList.get(0);

        BorrowDetailDAO.deleteBorrowDetailFromDatabase(toDelete);

        BorrowDetail deletedBorrowDetail = BorrowDetailDAO.getBorrowDetailById(toDelete.getId());
        assertEquals(null, deletedBorrowDetail);
    }
}
