package com.lims.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

interface AuthenticationControllerInterface {
    void authenticate(String email, String password);
    void deleteAuthentication();
    boolean isAuthenticated();
}

public class AuthenticationController implements AuthenticationControllerInterface {

    private final String authDataPath = "src/main/resources/auth/data.txt";

    private File checkFile() throws IOException {
        File authData = new File(this.authDataPath);
        if (authData.createNewFile()) {
            System.out.println("AUTH FIlE NOT FOUND, recreating auth data file");
        }
        return authData;
    }

    /**
     * @since 10-8-2024
     */
    @Override
    public void authenticate(String email, String password) {
        try {
            File authFile = this.checkFile();
            FileWriter fileWriter = new FileWriter(authFile);

            Base64.Encoder enc = Base64.getEncoder();

            String concatenated = email + ":" + password;
            String encoded = enc.encodeToString(concatenated.getBytes());

            fileWriter.write(encoded);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred, please try again later...");
            e.printStackTrace();
        }
    }

    /**
     * @since 10-8-2024
     */
    @Override
    public void deleteAuthentication() {
        try {
            File authFile = this.checkFile();
            FileWriter fileWriter = new FileWriter(authFile);

            fileWriter.write("");
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred, please try again later...");
            e.printStackTrace();
        }
    }

    /**
     * @since 10-8-2024
     */
    @Override
    public boolean isAuthenticated() {
        try {
            File authFile = this.checkFile();
            Scanner scanner = new Scanner(authFile);
            Base64.Decoder dec = Base64.getDecoder();

            if (!scanner.hasNextLine()) {
                return false;
            }

            String encoded = scanner.nextLine();
            String[] data = (new String(dec.decode(encoded))).split(":");
            System.out.println(data[0] + " " + data[1]);

            return true;
        } catch (IOException e) {
            System.out.println("NO AUTH FILE FOUND, please sign in...");
            return false;
        }

    }
}