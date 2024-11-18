package org.example;

import com.lims.controller.AuthenticationController;
import com.lims.controller.AuthorizationController;

public class Main {

    public static void main(String[] args) {
        System.out.println("Data core");

        AuthenticationController.deleteAuthentication();

        if (!AuthenticationController.isAuthenticated()) {
            AuthenticationController.authenticate("trinhngocthong@gmail.com", "tnt123");
        }

        try {
            System.out.println(AuthorizationController.isAuthorized(0) ? "Admin" : "User");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}