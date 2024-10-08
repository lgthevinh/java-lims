package com.lims.controller;

interface UserControllerInterface {
    boolean isUserLibrarian(Integer userId);
    boolean isUserStudent(Integer userId);
}

public class UserController implements UserControllerInterface {

    /**
     * {@code isUserLibrarian}
     *
     * <p>Check if given user has librarian privileges</p>
     *
     * @param userId The user id in database.
     * @return {@code true} if the user is librarian, {@code false} otherwise
     * @since 10-08-2024
     */
    @Override
    public boolean isUserLibrarian(Integer userId) {
        return false;
    }

    /**
     * {@code isUserStudent}
     *
     * <p>Check if given user has student privileges</p>
     *
     * @param userId The user id in database.
     * @return {@code true} if the user is student, {@code false} otherwise
     * @since 10-08-2024
     */
    @Override
    public boolean isUserStudent(Integer userId) {
        return false;
    }
}
