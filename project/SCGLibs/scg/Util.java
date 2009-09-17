package scg;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

/** Various Common utility functions */
public class Util {

    public static final double DELTA = 0.0001;
    static DecimalFormat form = new DecimalFormat("0.000");

    /** returns true if the first double is less than the second */
    public static boolean lessThan(double dec, double mindec){
        return dec + DELTA < mindec;
    }

    /** Format a double for three decimal places */
    public static String format(double d){
        return form.format(d);
    }

    /** Create a span that is a given color */
    public static String color(String s, String color){
        return tag(s, "span", "style=\"color:" + color + "\"");
    }

    /** Add an HTML tag to some text */
    public static String tag(String s, String with){
        return tag(s, with, "");
    }

    /** Add an HTML tag to some text with extra attributes */
    public static String tag(String s, String with, String attrs){
        return "<" + with + " " + attrs + ">" + s + "</" + with + ">";
    }

    static Random rand = new Random();

    /** Random Double between 0..1 */
    public static double random(){
        return rand.nextDouble();
    }

    /** Random Integer between 0..(bound-1) */
    public static int random(int bound){
        return rand.nextInt(bound);
    }

    /** Random coin flip of the given bias */
    public static boolean coinFlip(double bias){
        return Util.random() < bias;
    }

    /** Random coin flip, bias of 0.5 */
    public static boolean coinFlip(){
        return coinFlip(0.5);
    }

    /** Date Format for parsing Dates */
    public static final DateFormat DATEFMT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
    /** Set the current Time Zone for Date Parsing */
    static {
        DATEFMT.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
        // DateFmt.setTimeZone(TimeZone.getTimeZone("EST"));
    }

    /** Get the current Date/Time */
    public static Date now(){
        return new Date();
    }

    /** Parse a Date from the given String */
    public static Date parseDate(String date) throws java.text.ParseException{
        return DATEFMT.parse(date);
    }

    /** Print a Date in Shortened Format */
    public static String printDate(Date date){
        return DATEFMT.format(date);
    }

    public static String getFileNameSafeDate(Date date){
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        return "_" + c.get(Calendar.YEAR) + "_" + (c.get(Calendar.MONTH) + 1) + "_" + c.get(Calendar.DAY_OF_MONTH)
                + "_" + c.get(Calendar.HOUR_OF_DAY) + "_" + c.get(Calendar.MINUTE) + "_" + c.get(Calendar.SECOND);
    }

    /** Is the first Date after the Second Date? */
    public static boolean after(Date d1, Date d2){
        return d1.compareTo(d2) > 0;
    }

    /** Get a Semi-unique Log file name */
    public static String logFileName(String prefix){
        return prefix + "_" + System.currentTimeMillis() / 1000 + ".log";
    }

    /** Hex digits for output */
    private static final String HEX = "0123456789ABCDEF";
    /** Default Hashed Length */
    private static final int DEFAULT_LEN = 16;
    /** Hash implementation */
    private static MessageDigest hasher;
    static {
        try {
            hasher = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) { /* Serious Problems */
        }
    }

    /** Encrypt a given Password (consistently) */
    public static String encrypt(String s){
        return encrypt(s, DEFAULT_LEN);
    }

    /** Encrypt a given Password (consistently) */
    public static String encrypt(String s, int finalLen){
        synchronized (hasher) {
            hasher.reset();
            hasher.update(s.getBytes());
            byte[] bytes = hasher.digest();
            String ret = "";
            for (int i = 0; i < bytes.length && i < finalLen / 2; i++) {
                ret += "" + HEX.charAt(bytes[i] >> 4 & 0xF) + HEX.charAt(bytes[i] & 0xF);
            }
            return ret;
        }
    }

    /** Decode an encoded URL */
    public static String decodeURL(String url){
        return decodeURL(url, "UTF-8");
    }

    /** Decode an encoded URL */
    public static String decodeURL(String url, String enc){
        try {
            return java.net.URLDecoder.decode(url, enc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** How long to wait before re-checking */
    private static final int TIMEOUT = 5;

    /** Wait until the given Date */
    public static void waitUntil(Date when){
        while (when.compareTo(now()) > 0) {
            try {
                Thread.sleep(1000 * TIMEOUT);
            } catch (InterruptedException ie) {
            }
        }
    }

    public static <E> edu.neu.ccs.demeterf.lib.List<E> toDemF(Iterable<E> orig){
        return edu.neu.ccs.demeterf.lib.List.create(orig);
    }
}
