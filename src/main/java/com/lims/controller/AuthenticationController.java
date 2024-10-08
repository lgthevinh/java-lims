package com.lims.controller;

import com.lims.model.User;

interface AuthenticationControllerInterface {
    void authenticateUser(User user);

    void deleteAuthentication();

    boolean isUserAuthenticated(User user);
}

public class AuthenticationController implements AuthenticationControllerInterface {
    private User authenticatedUser;

    /**
     * {@code authenticateUser}
     *
     * <p>Authenticate user by storing authenticated user object into variable (could use encrypt data in the future)</p>
     *
     * @param user The user object in com.lims.model.User.
     * @since 10-8-2024
     */
    @Override
    public void authenticateUser(User user) {

    }

    /**
     * {@code deleteAuthentication}
     *
     * <p>Delete user authentication data from authenticatedUser variable</p>
     *
     * @since 10-8-2024
     */
    @Override
    public void deleteAuthentication() {

    }

    @Override
    public boolean isUserAuthenticated(User user) {
        return false;
    }
}