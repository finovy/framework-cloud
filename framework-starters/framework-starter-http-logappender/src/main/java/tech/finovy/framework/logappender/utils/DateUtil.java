package tech.finovy.framework.logappender.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateUtil {
    // RFC 822 Date Format
    private static final String RFC822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";

    private DateUtil() {
    }

    public static String formatRfc822Date(Date date) {
        return getRfc822DateFormat().format(date);
    }


    public static Date parseRfc822Date(String dateString) throws ParseException {
        return getRfc822DateFormat().parse(dateString);
    }

    private static DateFormat getRfc822DateFormat() {
        SimpleDateFormat rfc822DateFormat =
                new SimpleDateFormat(RFC822_DATE_FORMAT, Locale.US);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return rfc822DateFormat;
    }
}
