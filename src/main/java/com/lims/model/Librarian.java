package com.lims.model;

import java.util.Date;

public class Librarian extends User {
    private Integer empId;

    public Librarian(Integer userId, Integer socialId, String name, Date dateOfBirth, String addressLine, String phoneNumber, String email, String password) {
        super(socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }
}
