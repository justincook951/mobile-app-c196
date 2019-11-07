package com.example.c196project.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Standardizer
{

    public static String standardizeDateString(Date startDate, Date endDate)
    {
        String startFormatted = standardizeSingleDateString(startDate);
        String endFormatted = standardizeSingleDateString(endDate);
        String termDateString = startFormatted + " - " + endFormatted;
        return termDateString;
    }

    public static String standardizeSingleDateString(Date date)
    {
        String datePattern = "dd-MMM-yyyy";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
        return dateFormatter.format(date);
    }

    public static String dateStringFromComponents(int year, int month, int day)
    {
        Date newDate = dateFromComponents(year, month, day);
        return standardizeSingleDateString(newDate);
    }

    public static Date dateFromComponents(int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0);
        return cal.getTime();
    }
}
