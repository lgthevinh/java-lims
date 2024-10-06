package com.lims;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDatetime(String dateFormat, Date date) {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }
}
