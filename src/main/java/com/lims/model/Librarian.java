package com.lims.model;

import java.util.Date;

public class Librarian extends User {
    private Integer emp_id;
    private Date start_date;
    private Date end_date;

    public Librarian(Integer emp_id, Integer userId, Integer socialId, String name, Date dateOfBirth, String addressLine, String phoneNumber, String email, String password) {
        super(userId, socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);
        this.start_date = new Date();
        this.emp_id = emp_id;
    }

    public Integer getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(Integer emp_id) {
        this.emp_id = emp_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
}
