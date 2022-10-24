import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtilsTest {

    @Test(description = "Check getting number of the day in the month", dataProvider = "getDayInput")
    public void testGetDay(Date input, int expected, String message) {
        int result = DateUtils.getDay(input);
        Assert.assertEquals(result, expected, message);
    }

    @Test
    public void testGetMonth() {
    }

    @Test
    public void testGetYear() {
    }

    @Test
    public void testDaysInMonth() {
    }

    @DataProvider
    public Object[][] getDayInput() {
        return new Object[][] {
                {Date.from(new GregorianCalendar(120, Calendar.JANUARY, 1).toInstant()),  1, "Get 1st day"},  //y + 1900
                {Date.from(new GregorianCalendar(121, Calendar.FEBRUARY, 28).toInstant()), 28, "Get last day of Feb 2021"},
                {Date.from(new GregorianCalendar(120, Calendar.FEBRUARY, 29).toInstant()), 29, "Get last day of Feb 2020"},
                {Date.from(new GregorianCalendar(122, Calendar.SEPTEMBER, 30).toInstant()), 30, "Get last day of Sep"},
                {Date.from(new GregorianCalendar(122, Calendar.OCTOBER, 31).toInstant()), 31, "Get last day of Oct"}
        };
    }
}