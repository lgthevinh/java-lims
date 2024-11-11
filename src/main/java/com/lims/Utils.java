package com.lims;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Utils {
    public static String formatDatetime(String dateFormat, Date date) {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }

    public static Date convertStringToDatetime(String dateFormat, String dateString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(false);
        return formatter.parse(dateString);
    }

    public static Date promptForValidDate(Scanner scanner) {
        String dateFormat = "dd-MM-yyyy";
        Date date;

        while (true) {
            String dateString = scanner.nextLine();

            try {
                date = convertStringToDatetime(dateFormat, dateString);
                break;
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please enter again: ");
            }
        }
        return date;
    }

    public static boolean isStrongPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }
        return true;
    }

    public static int promptForValidYear(Scanner scanner) {
        int year;

        while (true) {
            System.out.print("Enter Year of Publication (yyyy): ");
            String yearString = scanner.nextLine();

            try {
                year = Integer.parseInt(yearString);
                if ((year < 0) || (year > 2024)) {
                    throw new NumberFormatException("Year cannot be negative.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid year. Please enter again.");
            }
        }
        return year;
    }
}
