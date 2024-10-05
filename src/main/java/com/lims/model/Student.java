package com.lims.model;

import java.util.Date;

public class Student extends User {
    private String studentId;
    private String school;
    private String major;

    public Student(Integer userId, Integer socialId, String name, Date dateOfBirth, String addressLine, String phoneNumber, String email, String password, Integer studentId, String school, String major) {
        super(userId, socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
