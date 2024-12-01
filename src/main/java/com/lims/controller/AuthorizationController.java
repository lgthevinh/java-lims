package com.lims.controller;

import com.lims.dao.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

public class AuthorizationController {

    private final AuthorizationController authorizationController = new AuthorizationController();

    private AuthorizationController() {
        System.out.println("Authorization controller created");
    }

    public AuthorizationController getInstance() {
        return this.authorizationController;
    }

    static final String authDataPath = "src/main/resources/auth/data.txt";

    private static File getFile() throws IOException {
        File authData = new File(authDataPath);
        if (authData.createNewFile()) {
            System.out.println("AUTH FIlE NOT FOUND, recreating auth data file...");
        }
        return authData;
    }

    public static boolean isAuthorized(Integer role) { // 0: Admin/Librarian 1:User
        try {
            File authFile = getFile();
            Scanner scanner = new Scanner(authFile);
            Base64.Decoder dec = Base64.getDecoder();

            if (!scanner.hasNextLine()) {
                return false;
            }

            String encoded = scanner.nextLine();
            String[] data = (new String(dec.decode(encoded))).split(":");

            if (DatabaseManager.getLibrarianById(Integer.valueOf(data[0])) != null && role == 0) {
                return true;
            }

            return DatabaseManager.getUserById(Integer.valueOf(data[0])) != null && role == 1;

        } catch (Exception e) {
            System.out.println("An error occurred, please try again...");
            e.printStackTrace();
            return false;
        }
    }

}
