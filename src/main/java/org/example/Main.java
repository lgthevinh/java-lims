package org.example;

import com.lims.controller.AuthenticationController;

public class Main {

    static AuthenticationController authController = new AuthenticationController();

    public static void main(String[] args) {
        System.out.println("Data-core");
        authController.authenticate("everwellmax@gmail.com", "123456");
        if (authController.isAuthenticated()) {
            System.out.println("Authenticated");
        } else {
            System.out.println("Not Authenticated");
        }
        authController.deleteAuthentication();
    }
}