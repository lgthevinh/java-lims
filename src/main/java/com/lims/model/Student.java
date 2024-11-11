package com.lims.model;

import java.util.Date;

public class Student extends User {
    private String studentId;
    private String school;
    private String major;

    public Student() {
    }

    public Student(User user) {
        super(user.getSocialId(), user.getName(), user.getDateOfBirth(), user.getAddressLine(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
    }

    public Student(String socialId, String name, Date dateOfBirth, String addressLine, String phoneNumber, String email, String password, String studentId, String school, String major) {
        super(socialId, name, dateOfBirth, addressLine, phoneNumber, email, password);
        this.studentId = studentId;
        this.school = school;
        this.major = major;
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
