package org.example;

import com.lims.controller.AuthenticationController;

public class Main {

    private static void printHelloWorld() {
        System.out.println("Hello World from LiMS");
    }

    public static void main(String[] args) {
        System.out.println("Data-core");

        AuthenticationController.authenticate("everwellmax@gmail.com", "123456");

        if (AuthenticationController.isAuthenticated()) {
            printHelloWorld();
        } else {
            System.out.println("Not Authenticated, unable to print hello world");
        }

        AuthenticationController.deleteAuthentication();
    }
}