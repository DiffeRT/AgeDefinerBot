import java.util.Date;

public class AgeDefiner {

    public static AgeDuration Diff(Date date_2, Date date_1) {
        int year2  = DateUtils.getYear(date_2);
        int month2 = DateUtils.getMonth(date_2);
        int day2   = DateUtils.getDay(date_2);
        int year1  = DateUtils.getYear(date_1);
        int month1 = DateUtils.getMonth(date_1);
        int day1   = DateUtils.getDay(date_1);

        int days, months, years;

        if (date_2.compareTo(date_1) >= 0) {
            if (day2 >= day1) {
                days = day2 - day1;
            }
            else {
                month2--;
                days = (day2 + DateUtils.daysInMonth(year1, month1) - day1);
            }
            if (month2 >= month1) {
                months = month2 - month1;
            }
            else {
                year2--;
                months = (month2 + 12) - month1;
            }
            years = year2 - year1;
            return new AgeDuration(years, months, days, false);
        }
        else {
            return Diff(date_1, date_2).unaryNegative();
        }
    }

    public static AgeDuration Age(Date date) {
        //TODO: Think about time zones. If server is run in one time zone, but client accessing from another one
        Date localTMZDate = new Date();
        return Diff(localTMZDate, date);
    }
}
