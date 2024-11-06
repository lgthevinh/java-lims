package com.lims.controller;

import com.lims.dao.DatabaseManager;
import com.lims.model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

public class AuthenticationController {

    static final String authDataPath = "src/main/resources/auth/data.txt";

    private static File getFile() throws IOException {
        File authData = new File(authDataPath);
        if (authData.createNewFile()) {
            System.out.println("AUTH FIlE NOT FOUND, recreating auth data file...");
        }
        return authData;
    }

    /**
     * @since 10-8-2024
     */
    public static void authenticate(String email, String password) {
        try {
            User user = DatabaseManager.getUserByEmail(email);

            if (user == null) {
                System.out.println("User not found, please try again...");
                return;
            }

            if (!user.getPassword().equals(password)) {
                System.out.println("Wrong password, please try again...");
                return;
            }

            try {
                File authFile = getFile();
                FileWriter fileWriter = new FileWriter(authFile);

                Base64.Encoder enc = Base64.getEncoder();

                String concatenated = user.getUserId() + ":" + user.getEmail() + ":" + user.getPassword();
                String encoded = enc.encodeToString(concatenated.getBytes());

                fileWriter.write(encoded);
                fileWriter.close();

                System.out.println("Authenticate success");
            } catch (Exception e) {
                System.out.println("An error occurred, please try again later...");
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred, please try again later...");
        }
    }

    /**
     * @since 11-3-2024
     */
    public static void deleteAuthentication() {
        try {
            File authFile = getFile();
            FileWriter fileWriter = new FileWriter(authFile);

            fileWriter.write("");
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred, please try again later...");
            e.printStackTrace();
        }
    }

    /**
     * @since 11-5-2024
     */
    public static boolean isAuthenticated() {
        try {
            File authFile = getFile();
            Scanner scanner = new Scanner(authFile);
            Base64.Decoder dec = Base64.getDecoder();

            if (!scanner.hasNextLine()) {
                return false;
            }

            String encoded = scanner.nextLine();
            String[] data = (new String(dec.decode(encoded))).split(":");

            User user = DatabaseManager.getUserById(Integer.valueOf(data[0]));

            if (user == null) {
                return false;
            }

            return user.getEmail().equals(data[1]) && user.getPassword().equals(data[2]);
        } catch (Exception e) {
            System.out.println("An error occurred, please try again...");
            e.printStackTrace();
            return false;
        }
    }
}