package com.lims.model;

import java.util.Date;

public class User {
    private Integer userId;
    private String socialId;
    private String name;
    private Date dateOfBirth;
    private String addressLine;
    private String phoneNumber;
    private String email;
    private String password;

    public User() {
    }

    public User(String socialId, String name, Date dateOfBirth, String addressLine, String phoneNumber, String email, String password) {
        this.socialId = socialId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.addressLine = addressLine;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
