package com.lims.model;

public class Librarian extends User {
    private Integer empId;

    public Librarian(User user) {
        super(user.getSocialId(), user.getName(), user.getDateOfBirth(), user.getAddressLine(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
        this.setUserId(user.getUserId());
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }
}
