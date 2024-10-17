package com.lims.model;

import java.util.Date;

public class Librarian extends User {
    private Integer empId;

    public Librarian() {
    }

    public Librarian(User user) {
        super(user.getSocialId(), user.getName(), user.getDateOfBirth(), user.getAddressLine(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
        this.setUserId(user.getUserId());
    }

    public Librarian(String socialId, String name, Date dateOfBirth, String addressLine, String phoneNumber, String email, String password, Integer empId) {
        super(socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);
        this.empId = empId;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }
}
