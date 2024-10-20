package com.lims.model;

public class Student extends User {
    private String studentId;
    private String school;
    private String major;

    public Student(User user) {
        super(user.getSocialId(), user.getName(), user.getDateOfBirth(), user.getAddressLine(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
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
