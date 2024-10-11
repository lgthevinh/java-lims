package com.lims.model;

import java.util.Date;

public class BorrowDetail {
    private Integer id;
    private String bookIsbn;
    private Integer borrowerId;
    private Integer librarianId;
    private Date borrowDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;

    public BorrowDetail(String bookIsbn, Integer borrowerId, Integer librarianId, Date borrowDate, Date expectedReturnDate, Date actualReturnDate) {
        this.bookIsbn = bookIsbn;
        this.borrowerId = borrowerId;
        this.librarianId = librarianId;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Integer getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(Integer librarianId) {
        this.librarianId = librarianId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
}
