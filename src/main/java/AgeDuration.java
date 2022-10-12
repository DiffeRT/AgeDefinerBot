public class AgeDuration {
    private final int AVG_MONTH = 30;  //The Gregorian (western) solar calendar has 365.2425/12 = 30.44 days
    private final boolean isNegative;
    private final int y, m, d;

    public AgeDuration(int years, int months, int days, boolean isNegative) {
        this.isNegative = isNegative;
        this.y = years;
        this.m = months;
        this.d = days;
    }

    public AgeDuration Add(AgeDuration other) {
        if (this.isNegative == other.isNegative) {
            int years = this.y + other.y;
            int months = this.m + other.m;
            int days = this.d + other.d;

            if (days > AVG_MONTH) {
                days = days - AVG_MONTH;
                months++;
            }
            if (months >= 12) {
                months = months - 12;
                years++;
            }

            return new AgeDuration(years, months, days, this.isNegative);
        }
        else if (other.isNegative) {
            return this.Sub(other);
        }
        else {
            return other.Sub(this);
        }
    }

    public AgeDuration Sub(AgeDuration other) {
        if (this.greaterThan(other)) {
            int years = this.y - other.y;
            int months = this.m - other.m;
            int days = this.d - other.d;

            if (days < 0) {
                days = days + AVG_MONTH;
                months--;
            }
            if (months < 0) {
                months = months + 12;
                years--;
            }
            return new AgeDuration(years, months, days, false);
        }
        else {
            int years = other.y - this.y;
            int months = other.m - this.m;
            int days = other.d - this.d;

            if (days < 0) {
                days = days + AVG_MONTH;
                months--;
            }
            if (months < 0) {
                months = months + 12;
                years--;
            }
            return new AgeDuration(years, months, days, true);
        }
    }

    public AgeDuration unaryNegative() {
        return new AgeDuration(y, m, d, !isNegative);
    }

    @Override
    public String toString() {
        String age_length;
        if (y > 0) {
            age_length = String.format("%d %s %d %s %d %s", y, getDimName("year", y), m, getDimName("month", m), d, getDimName("day", d));
        }
        else if (this.m > 0) {
            age_length = String.format("%d %s %d %s", m, getDimName("month", m), d, getDimName("day", d));
        }
        else {
            age_length = String.format("%d %s", d, getDimName("day", d));
        }
        if (this.isNegative) {
            age_length = "- " + age_length;
        }
        return age_length;
    }

    private String getDimName(String dim, int value) {
        if (value != 1) {
            return dim + "s";
        }
        else {
            return dim;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AgeDuration) {
            AgeDuration other = (AgeDuration) obj;
            return this.isNegative == other.isNegative && this.y == other.y && this.m == other.m && this.d == other.d;
        }
        else {
            return false;
        }
    }

    public boolean greaterThan(AgeDuration other) {
        if (this.y > other.y) {
            return true;
        }
        else if (this.y < other.y) {
            return false;
        }
        else if (this.m > other.m) {      //this.y == other.y
            return true;
        }
        else if (this.m < other.m) {
            return false;
        }
        else if (this.d > other.d) {      //this.m == other.m
            return true;
        }
        else return false;
    }

}
