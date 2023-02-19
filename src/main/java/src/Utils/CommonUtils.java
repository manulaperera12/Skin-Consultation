package src.Utils;

import src.Constants.Constants;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils {
    private CommonUtils() {}

    private static final DecimalFormat decimalFormatter = new DecimalFormat("0.00");
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT);

    /**
     * check contact number validity
     * Contact number must start with [94]: 94776787689
     */
    public static boolean isContactNumberValid(String contactNumber) {
        return contactNumber.matches("94\\d{9}");
    }

    /**
     * check email validity
     */
    public static boolean isEmailValid(String email) {
        return email.matches( "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    /**
     * Basic Encryptor
     */
    public static String encrypt(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    /**
     * Basic Decrypt
     */
    public static String decrypt(String encryptedText) {
        return new String(Base64.getDecoder().decode(encryptedText));
    }

    /**
     * @param value
     * @return
     */
    public static double formatToTwoDecimalPlaces(double value) {
        return Double.parseDouble(decimalFormatter.format(value));
    }

    /**
     * Format Date
     */
    public static String formatDate(Date date) {
        return dateFormatter.format(date);
    }

    /**
     * Parse Date
     * @throws ParseException
     */
    public static Date parseDate(String dateText) throws ParseException {
        return dateFormatter.parse(dateText);
    }

    /**
     * validate string, whether empty or not
     */
    public static boolean isEmptyString(String string) {
        return string.replace(" ", "").isEmpty();
    }

    /**
     * check the @param contains numbers only
     */
    public static boolean numbersOnly(String prm) {
        return prm.length() > 0 && prm.matches("[0-9]*");
    }

    /**
     * Remove Whitespaces
     */
    public static String removeWhitespaces(String string) {
        return string.replace(" ", "");
    }

    /**
     * returns the date with morning time
     * 2019-11-21 11:21:21:001 -> 2019-11-21 00:00:00:000
     */
    public static Date getMorningDate(Date d1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * get number of days between Date date1 & Date date2
     */
    public static long getDaysInBetween(Date date1, Date date2){
        date1 = getMorningDate(date1);
        date2 = getMorningDate(date2);
        return ChronoUnit.DAYS.between(LocalDate.parse(dateFormatter.format(date1)), LocalDate.parse(dateFormatter.format(date2)));
    }

    /**
     * check if Date d1 & Date d2 are same days
     */
    public static boolean areDaysSame(Date d1, Date d2) {
        return getDaysInBetween(d1, d2) == 0;
    }

    /**
     * check if Date d1 is behind Date d2
     */
    public static boolean isDayBehind(Date d1, Date d2){
        return getDaysInBetween(d1, d2) < 0;
    }

    /**
     * check if Date d1 is ahead of Date d2
     */
    public static boolean isDayAhead(Date d1, Date d2){
        return getDaysInBetween(d1, d2) > 0;
    }

    /**
     * check if the given date is a future date
     */
    public static boolean isFutureDate(Date d1) {
        return isDayAhead(new Date(), d1);
    }

    /**
     * checks if the given date is today
     */
    public static boolean isTodayDate(Date d1) {
        return areDaysSame(new Date(), d1);
    }
}
