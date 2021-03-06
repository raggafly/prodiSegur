package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper functions for handling dates.
 * 
 * @author Marco Jakob
 */
public class DateUtil {
	
	/** The date pattern that is used for conversion. Change as you wish. */
	private static final String DATE_PATTERN = "dd.MM.yyyy";
	
	/** The date formatter. */
	private static final DateTimeFormatter DATE_FORMATTER = 
			DateTimeFormatter.ofPattern(DATE_PATTERN);
	
	/**
     * Returns the given date as a well formatted String. The above defined 
     * {@link DateUtil#DATE_PATTERN} is used.
     * 
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN} 
     * to a {@link LocalDate} object.
     * 
     * Returns null if the String could not be converted.
     * 
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */
    public static LocalDate parse(String dateString) {
        try {
        	return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static Date String2utilDate(String ddMMyyyy){
    	DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    	Date date = new Date();
		try {
			date = formatter.parse(ddMMyyyy);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(date);
    	return date;
    }
    
    public static LocalDate dateToLocalDate (Date fecha){
    	LocalDate date = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	return date;
    }
   
    public static Date LocalDateToDate (LocalDate fecha){
    	Date date = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	return date;
    }

    /**
     * Checks the String whether it is a valid date.
     * 
     * @param dateString
     * @return true if the String is a valid date
     */
    public static boolean validDate(String dateString) {
    	// Try to parse the String.
    	return DateUtil.parse(dateString) != null;
    }
    
    public static boolean isNumeric(String s) {  
        return s.matches("[-+]?\\d*\\.?\\d+");  
    }
    
    public static boolean isNumericInteger(String s) {  
        return s.matches("\\d*");  
    }
    
    public static String formatUtilDate(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

     // Get the date today using Calendar object.
//     date = Calendar.getInstance().getTime();        
     // Using DateFormat format method we can create a string 
     // representation of a date with the defined format.
     String fechaFormateada = df.format(date);

        return fechaFormateada;
    }
}
