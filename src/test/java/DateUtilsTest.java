import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtilsTest {

    @Test(description = "Check number of the day in the month", dataProvider = "getDayInput")
    public void testGetDay(Date input, int expected, String message) {
        int result = DateUtils.getDay(input);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check number of the month", dataProvider = "getMonthInput")
    public void testGetMonth(Date input, int expected, String message) {
        //Expect numeration from the 1
        int result = DateUtils.getMonth(input);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check number of the year", dataProvider = "getYearInput")
    public void testGetYear(Date input, int expected, String message) {
        int result = DateUtils.getYear(input);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check count of the days in the month", dataProvider = "getDaysInMonthInput")
    public void testDaysInMonth(int inYear, int inMonth, int expected, String message) {
        int result = DateUtils.daysInMonth(inYear, inMonth);
        Assert.assertEquals(result, expected, message);
    }

    @DataProvider
    public Object[][] getDayInput() {
        return new Object[][] {
                {Date.from(new GregorianCalendar(2020, Calendar.JANUARY, 1).toInstant()),     1, "Get 1st day"},               //01.01.2020
                {Date.from(new GregorianCalendar(2021, Calendar.FEBRUARY, 28).toInstant()),  28, "Get last day of Feb 2021"},  //28.02.2021
                {Date.from(new GregorianCalendar(2020, Calendar.FEBRUARY, 29).toInstant()),  29, "Get last day of Feb 2020"},  //29.02.2020
                {Date.from(new GregorianCalendar(2022, Calendar.SEPTEMBER, 30).toInstant()), 30, "Get last day of Sep"},       //30.09.2022
                {Date.from(new GregorianCalendar(2022, Calendar.OCTOBER, 31).toInstant()),   31, "Get last day of Oct"}        //31.10.2022
        };
    }

    @DataProvider
    public Object[][] getMonthInput() {
        return new Object[][] {
                {Date.from(new GregorianCalendar(2022, Calendar.JANUARY, 1).toInstant()),     1, "Get 1st month"},  //01.01.2022
                {Date.from(new GregorianCalendar(2022, Calendar.JUNE, 30).toInstant()),       6, "Get 6th month"},  //30.06.2022
                {Date.from(new GregorianCalendar(2022, Calendar.DECEMBER, 31).toInstant()),  12, "Get 12th month"}  //31.12.2022
        };
    }

    @DataProvider
    public Object[][] getYearInput() {
        return new Object[][] {
                {Date.from(new GregorianCalendar(1970, Calendar.JANUARY, 1).toInstant()),   1970, "Get 1970"},  //01.01.1970
                {Date.from(new GregorianCalendar(1999, Calendar.DECEMBER, 31).toInstant()), 1999, "Get 1999"},  //31.12.1999
                {Date.from(new GregorianCalendar(2000, Calendar.JANUARY, 1).toInstant()),   2000, "Get 2000"},  //01.01.2000
                {Date.from(new GregorianCalendar(2049, Calendar.MAY, 15).toInstant()),      2049, "Get 2049"}   //15.05.2049
        };
    }

    @DataProvider
    public Object[][] getDaysInMonthInput() {
        return new Object[][] {
                {2022,  1,  31, "31 days in Jan"},
                {2020,  2,  29, "29 days in Feb 2020"},
                {2022,  2,  28, "28 days in Feb 2022"},
                {2022,  3,  31, "31 days in Mar"},
                {2022,  4,  30, "30 days in Apr"},
                {2022,  5,  31, "31 days in May"},
                {2022,  6,  30, "30 days in Jun"},
                {2022,  7,  31, "31 days in Jul"},
                {2022,  8,  31, "31 days in Aug"},
                {2022,  9,  30, "30 days in Sep"},
                {2022, 10,  31, "31 days in Oct"},
                {2022, 11,  30, "30 days in Nov"},
                {2022, 12,  31, "31 days in Dec"}
        };
    }
}