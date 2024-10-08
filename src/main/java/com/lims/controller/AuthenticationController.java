package com.lims.controller;

import com.lims.model.User;

interface AuthenticationControllerInterface {
    void authenticateUser(User user);

    void deleteAuthentication();

    boolean isUserAuthenticated(User user);
}

public class AuthenticationController implements AuthenticationControllerInterface {

    @Override
    public void authenticateUser(User user) {

    }

    @Override
    public void deleteAuthentication() {

    }

    @Override
    public boolean isUserAuthenticated(User user) {
        return false;
    }
}