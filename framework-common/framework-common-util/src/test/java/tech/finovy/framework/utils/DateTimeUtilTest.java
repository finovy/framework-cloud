package tech.finovy.framework.utils;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;

class DateTimeUtilTest {
    
    public static final String dateStr = "2023-10-12 00:00:00";

    public static final String onlyDateStr = "2023-10-12";

    public static final String onlyHourStr = "00:00:00";


    public static final String pattern = "yyyy-MM-dd HH:mm:ss";
    
    public static final LocalDateTime temporal = LocalDateTime.of(2023,10,12, 0,0,0);

    public static final LocalDateTime temporal_8 = LocalDateTime.of(2023,10,11, 16,0,0);

    public static final LocalDateTime temporal_24 = LocalDateTime.of(2023,10,11, 0,0,0);
    public static final LocalDate date = LocalDate.of(2023, 10, 12);

    @Test
    void testFormatDateTime() {
        // Run the test
        final String result = DateTimeUtil.formatDateTime(temporal);
        // Verify the results
        assertThat(result).isEqualTo(dateStr);
    }

    @Test
    void testFormatDate() {
        // Run the test
        final String result = DateTimeUtil.formatDate(temporal);
        // Verify the results
        assertThat(result).isEqualTo(onlyDateStr);
    }

    @Test
    void testFormatTime() {
        // Run the test
        final String result = DateTimeUtil.formatTime(temporal);
        // Verify the results
        assertThat(result).isEqualTo("00:00:00");
    }

    @Test
    void testFormat() {
        // Run the test
        final String result = DateTimeUtil.format(temporal, pattern);
        // Verify the results
        assertThat(result).isEqualTo(dateStr);
    }

    @Test
    void testParseDateTime1() {
        assertThat(DateTimeUtil.parseDateTime(dateStr, pattern)).isEqualTo(temporal);
    }

    @Test
    void testParseDateTime2() {
        // Setup
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Run the test
        final LocalDateTime result = DateTimeUtil.parseDateTime(dateStr, formatter);

        // Verify the results
        assertThat(result).isEqualTo(temporal);
    }

    @Test
    void testParseDateTime3() {
        assertThat(DateTimeUtil.parseDateTime(dateStr)).isEqualTo(temporal);
    }

    @Test
    void testParseDate1() {
        assertThat(DateTimeUtil.parseDate(dateStr, pattern)).isEqualTo(date);
    }

    @Test
    void testParseDate2() {
        // Setup
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Run the test
        final LocalDate result = DateTimeUtil.parseDate(dateStr, formatter);

        // Verify the results
        assertThat(result).isEqualTo(date);
    }

    @Test
    void testParseDate3() {
        assertThat(DateTimeUtil.parseDate(onlyDateStr)).isEqualTo(date);
    }

    @Test
    void testParseTime1() {
        assertThat(DateTimeUtil.parseTime(dateStr, pattern)).isEqualTo(LocalTime.of(00, 0, 0));
    }

    @Test
    void testParseTime2() {
        // Setup
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Run the test
        final LocalTime result = DateTimeUtil.parseTime(dateStr, formatter);

        // Verify the results
        assertThat(result).isEqualTo(LocalTime.of(0, 0, 0));
    }

    @Test
    void testParseTime3() {
        assertThat(DateTimeUtil.parseTime(onlyHourStr)).isEqualTo(LocalTime.of(0, 0, 0));
    }

    @Test
    void testToInstant() {
//        assertThat(DateTimeUtil.toInstant(temporal))
//                .isEqualTo(LocalDateTime.of(2023, 10, 11, 16, 0, 0, 0).toInstant(ZoneOffset.UTC));
    }

    @Test
    void testToDateTime() {
//        assertThat(
//                DateTimeUtil.toDateTime(temporal_8.toInstant(ZoneOffset.UTC)))
//                .isEqualTo(temporal);
    }

    @Test
    void testToDate() {
        assertThat(DateTimeUtil.toDate(temporal))
                .isEqualTo(new GregorianCalendar(2023, Calendar.OCTOBER, 12).getTime());
    }

    @Test
    void testBetween1() {
        // Setup
        final Temporal startInclusive = temporal_24;
        final Temporal endExclusive = temporal;
        final Duration expectedResult = Duration.ofDays(1L);

        // Run the test
        final Duration result = DateTimeUtil.between(startInclusive, endExclusive);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testBetween2() {
        assertThat(DateTimeUtil.between(date, date))
                .isEqualTo(Period.between(date, date));
    }

    @Test
    void testGetTimeStamp() {
        assertThat(DateTimeUtil.getTimeStamp()).isGreaterThan(1697126309674L);
    }
}
