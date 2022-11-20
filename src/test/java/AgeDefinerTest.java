import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AgeDefinerTest {

    @Test(description = "Check Diff() function. Eq Classing and Boundary", dataProvider = "getEqClassesAndBounds")
    public void testDiffEqClassesAndBounds(Date dateEnd, Date dateStart, AgeDuration expected, String message) {
        AgeDuration result = AgeDefiner.Diff(dateEnd, dateStart);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check Diff() function month transitions", dataProvider = "getMonthTransitions")
    public void testDiffMonthTransitions(Date dateEnd, Date dateStart, AgeDuration expected, String message) {
        AgeDuration result = AgeDefiner.Diff(dateEnd, dateStart);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check Diff() function additional", dataProvider = "getAdditional")
    public void testDiffAdditional(Date dateEnd, Date dateStart, AgeDuration expected, String message) {
        AgeDuration result = AgeDefiner.Diff(dateEnd, dateStart);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check Age() function")
    public void testAge() {
        Instant now = Instant.now();

        Date d = Date.from(now);
        AgeDuration result = AgeDefiner.Age(d);
        Assert.assertEquals(result, new AgeDuration(0, 0, 0, false), "Age(Today) == 0");

        Date before = Date.from(now.minus(Duration.ofDays(1)));
        result = AgeDefiner.Age(before);
        Assert.assertEquals(result, new AgeDuration(0, 0, 1, false), "Age(Today-1) == 1d");

        Date after = Date.from(now.plus(Duration.ofDays(1)));
        result = AgeDefiner.Age(after);
        Assert.assertEquals(result, new AgeDuration(0, 0, 1, true), "Age(Today+1) == -1d");
    }

    @DataProvider
    public Object[][] getEqClassesAndBounds() {
        return new Object[][] {
                //d2 > d1 and d, m, y transitions
                {Date.from(new GregorianCalendar(2022, Calendar.JUNE, 15).toInstant()), Date.from(new GregorianCalendar(2019, Calendar.APRIL, 5).toInstant()),
                        new AgeDuration(3, 2, 10, false), "Diff(15.06.2022, 05.04.2019) == 3y 2m 10d"},
                {Date.from(new GregorianCalendar(2022, Calendar.JUNE, 15).toInstant()), Date.from(new GregorianCalendar(2019, Calendar.APRIL, 25).toInstant()), //BUG
                        new AgeDuration(3, 1, 21, false), "Diff(15.06.2022, 25.04.2019) == 3y 1m 21d"},
                {Date.from(new GregorianCalendar(2022, Calendar.JUNE, 15).toInstant()), Date.from(new GregorianCalendar(2019, Calendar.JULY, 5).toInstant()),
                        new AgeDuration(2, 11, 10, false), "Diff(15.06.2022, 05.07.2019) == 2y 11m 10d"},
                {Date.from(new GregorianCalendar(2022, Calendar.JUNE, 15).toInstant()), Date.from(new GregorianCalendar(2019, Calendar.JUNE, 25).toInstant()),  //BUG
                        new AgeDuration(2, 11, 21, false), "Diff(15.06.2022, 25.06.2019) == 2y 11m 21d"},
                //Zero
                {Date.from(new GregorianCalendar(2022, Calendar.OCTOBER, 5).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.OCTOBER, 5).toInstant()),
                        new AgeDuration(0, 0, 0, false), "Diff(a, a) == 0"},
                //d2 < d1
                {Date.from(new GregorianCalendar(2019, Calendar.JULY, 5).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.JUNE, 15).toInstant()),
                        new AgeDuration(2, 11, 10, true), "Diff(05.07.2019, 15.06.2022) == -2y 11m 10d"},
                //Month high bounds
                {Date.from(new GregorianCalendar(2020, Calendar.MARCH, 24).toInstant()), Date.from(new GregorianCalendar(2020, Calendar.FEBRUARY, 29).toInstant()),
                        new AgeDuration(0, 0, 24, false), "Diff(24.03.2020, 29.02.2020) == 24d"},
                {Date.from(new GregorianCalendar(2022, Calendar.MARCH, 24).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.FEBRUARY, 28).toInstant()),
                        new AgeDuration(0, 0, 24, false), "Diff(24.03.2022, 28.02.2022) == 24d"},
                {Date.from(new GregorianCalendar(2022, Calendar.JUNE, 24).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.MAY, 31).toInstant()),
                        new AgeDuration(0, 0, 24, false), "Diff(24.06.2022, 31.05.2022) == 24d"},
                {Date.from(new GregorianCalendar(2022, Calendar.JULY, 24).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.JUNE, 30).toInstant()),
                        new AgeDuration(0, 0, 24, false), "Diff(24.06.2022, 30.06.2022) == 24d"},
                //Ones on diagonal
                {Date.from(new GregorianCalendar(2022, Calendar.FEBRUARY, 1).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.JANUARY, 31).toInstant()),
                        new AgeDuration(0, 0, 1, false), "Diff(a+1d, a) == 0.0.1"},
                {Date.from(new GregorianCalendar(2020, Calendar.APRIL, 26).toInstant()), Date.from(new GregorianCalendar(2020, Calendar.MARCH, 26).toInstant()),
                        new AgeDuration(0, 1, 0, false), "Diff(a+1m, a) == 0.1.0"},
                {Date.from(new GregorianCalendar(2020, Calendar.JUNE, 15).toInstant()), Date.from(new GregorianCalendar(2019, Calendar.JUNE, 15).toInstant()),
                        new AgeDuration(1, 0, 0, false), "Diff(a+1y, a) == 1.0.0"}
        };
    }

    @DataProvider
    public Object[][] getMonthTransitions() {
        return new Object[][] {
                //Month transition
                {Date.from(new GregorianCalendar(2022, Calendar.FEBRUARY, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.JANUARY, 26).toInstant()),
                        new AgeDuration(0, 0, 30, false), "Diff(25.02.2022, 26.01.2022) == 30d"},
                {Date.from(new GregorianCalendar(2022, Calendar.MARCH, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.FEBRUARY, 26).toInstant()),
                        new AgeDuration(0, 0, 27, false), "Diff(25.03.2022, 26.02.2022) == 27d"},
                {Date.from(new GregorianCalendar(2022, Calendar.APRIL, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.MARCH, 26).toInstant()),
                        new AgeDuration(0, 0, 30, false), "Diff(25.04.2022, 26.03.2022) == 30d"},
                {Date.from(new GregorianCalendar(2022, Calendar.MAY, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.APRIL, 26).toInstant()),
                        new AgeDuration(0, 0, 29, false), "Diff(25.05.2022, 26.04.2022) == 29d"},
                {Date.from(new GregorianCalendar(2022, Calendar.JUNE, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.MAY, 26).toInstant()),
                        new AgeDuration(0, 0, 30, false), "Diff(25.06.2022, 26.05.2022) == 30d"},
                {Date.from(new GregorianCalendar(2022, Calendar.JULY, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.JUNE, 26).toInstant()),
                        new AgeDuration(0, 0, 29, false), "Diff(25.07.2022, 26.06.2022) == 29d"},
                {Date.from(new GregorianCalendar(2022, Calendar.AUGUST, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.JULY, 26).toInstant()),
                        new AgeDuration(0, 0, 30, false), "Diff(25.08.2022, 26.07.2022) == 30d"},
                {Date.from(new GregorianCalendar(2022, Calendar.SEPTEMBER, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.AUGUST, 26).toInstant()),
                        new AgeDuration(0, 0, 30, false), "Diff(25.09.2022, 26.08.2022) == 30d"},
                {Date.from(new GregorianCalendar(2022, Calendar.OCTOBER, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.SEPTEMBER, 26).toInstant()),
                        new AgeDuration(0, 0, 29, false), "Diff(25.10.2022, 26.09.2022) == 29d"},
                {Date.from(new GregorianCalendar(2022, Calendar.NOVEMBER, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.OCTOBER, 26).toInstant()),
                        new AgeDuration(0, 0, 30, false), "Diff(25.11.2022, 26.10.2022) == 30d"},
                {Date.from(new GregorianCalendar(2022, Calendar.DECEMBER, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.NOVEMBER, 26).toInstant()),
                        new AgeDuration(0, 0, 29, false), "Diff(25.12.2022, 26.11.2022) == 29d"},
                {Date.from(new GregorianCalendar(2023, Calendar.JANUARY, 25).toInstant()), Date.from(new GregorianCalendar(2022, Calendar.DECEMBER, 26).toInstant()),
                        new AgeDuration(0, 0, 30, false), "Diff(25.01.2023, 26.12.2022) == 30d"},
                {Date.from(new GregorianCalendar(2020, Calendar.MARCH, 25).toInstant()), Date.from(new GregorianCalendar(2020, Calendar.FEBRUARY, 26).toInstant()),
                        new AgeDuration(0, 0, 28, false), "Diff(25.03.2020, 26.02.2020) == 28d"}
        };
    }

    @DataProvider
    public Object[][] getAdditional() {
        return new Object[][] {
                {Date.from(new GregorianCalendar(2009, Calendar.APRIL, 1).toInstant()), Date.from(new GregorianCalendar(2007, Calendar.OCTOBER, 1).toInstant()),
                        new AgeDuration(1, 6, 0, false), "Diff(01.04.2009, 01.10.2007) == 1y 6m 0d"},
                {Date.from(new GregorianCalendar(2012, Calendar.SEPTEMBER, 28).toInstant()), Date.from(new GregorianCalendar(2009, Calendar.JULY, 29).toInstant()),
                        new AgeDuration(3, 1, 30, false), "Diff(28.09.2012, 29.07.2009) == 3y 1m 30d"},
                {Date.from(new GregorianCalendar(2016, Calendar.DECEMBER, 30).toInstant()), Date.from(new GregorianCalendar(2013, Calendar.JULY, 22).toInstant()),
                        new AgeDuration(3, 5, 8, false), "Diff(30.12.2016, 22.07.2013) == 3y 5m 8d"},
                {Date.from(new GregorianCalendar(2022, Calendar.JUNE, 30).toInstant()), Date.from(new GregorianCalendar(2017, Calendar.FEBRUARY, 20).toInstant()),
                        new AgeDuration(5, 4, 10, false), "Diff(30.06.2022, 20.02.2017) == 5y 4m 10d"}
        };
    }
}