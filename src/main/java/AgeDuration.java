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
            return this.Sub(other.unaryNegative());
        }
        else {
            return other.Sub(this.unaryNegative());
        }
    }

    public AgeDuration Sub(AgeDuration other) {
        if (this.equals(other)) {
            return new AgeDuration(0, 0, 0, false);
        }

        if (this.isNegative && !other.isNegative || !this.isNegative && other.isNegative) {  // -a - b || a - (-b)
            return this.Add(other.unaryNegative());
        }
        else if (this.isNegative) {  // -a - (-b)
            return other.unaryNegative().Sub(this.unaryNegative());
        }
        else if (this.greaterThan(other)) {  // a - b
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
        if (this.equals(other)) {
            return true;
        }
        if (this.isNegative == other.isNegative) {
            if (this.y > other.y) {
                return !this.isNegative;
            }
            else if (this.y < other.y) {
                return this.isNegative;
            }
            else if (this.m > other.m) {
                //At this point we know -> this.y == other.y
                return !this.isNegative;
            }
            else if (this.m < other.m) {
                return this.isNegative;
            }
            else {
                //At this point we know -> this.m == other.m
                return (this.d > other.d) && !this.isNegative;
            }
        }
        else {
            return !this.isNegative;
        }
    }

}
