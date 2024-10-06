package com.lims.controller;

interface UserControllerInterface {
    boolean isUserLibrarian(Integer userId);

    boolean isUserStudent(Integer userId);
}

public class UserController implements UserControllerInterface {
    @Override
    public boolean isUserLibrarian(Integer userId) {
        return false;
    }

    @Override
    public boolean isUserStudent(Integer userId) {
        return false;
    }
}
