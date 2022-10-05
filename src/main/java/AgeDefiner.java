import java.util.Date;

public class AgeDefiner {

    public static AgeDuration Diff(Date date_2, Date date_1) {
        int d2_y = DateUtils.getYear(date_2);
        int d2_m = DateUtils.getMonth(date_2);
        int d2_d = DateUtils.getDay(date_2);
        int d1_y = DateUtils.getYear(date_1);
        int d1_m = DateUtils.getMonth(date_1);
        int d1_d = DateUtils.getDay(date_1);

        int days, months, years;

        if (d2_d >= d1_d) {
            days = d2_d - d1_d;
        }
        else {
            d2_m--;
            days = (d2_d + DateUtils.daysInMonth(d1_y, d1_m) - d1_d);
        }
        if (d2_m >= d1_m) {
            months = d2_m - d1_m;
        }
        else {
            d2_y--;
            months = (d2_m + 12) - d1_m;
        }
        years = d2_y - d1_y;
        return new AgeDuration(years, months, days, false);
    }

    public static AgeDuration Age(Date date) {
        //TODO: Think about time zones. If server is run in one time zone, but client accessing from another one
        Date localTMZDate = new Date();
        return Diff(localTMZDate, date);
    }
}
