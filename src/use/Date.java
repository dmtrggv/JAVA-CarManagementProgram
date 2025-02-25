package use;

import java.time.LocalDate;

public class Date {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public Date(LocalDate date) {

        if (date != null) {
            this.year = date.getYear();
            this.month = date.getMonthValue();
            this.day = date.getDayOfMonth();
            this.hour = 0;
            this.minute = 0;
            this.second = 0;
        } else {
            this.year = 0;
            this.month = 0;
            this.day = 0;
            this.hour = 0;
            this.minute = 0;
            this.second = 0;
        }

    }

    public Date(String fromString) {

        String[] dateFields = fromString.split("\\.");

        try {
            this.day = Integer.parseInt(dateFields[0]);
            this.month = Integer.parseInt(dateFields[1]);
            this.year = Integer.parseInt(dateFields[2]);

            this.month = (this.month % 12 == 0) ? 12 : (this.month % 12);  // Ensure month is between 1 and 12
            this.day = (this.day % 31 == 0) ? 31 : (this.day % 31);        // Ensure day is between 1 and 31

        } catch (NumberFormatException e) {
            // throw new IllegalArgumentException(fromString + " contains invalid numeric values.");
        }

    }

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month % 13;
        this.day = day % 32;
    }

    public Date(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = ((month - 1) % 12) + 1;
        this.day = ((day - 1) % 31) + 1;
        this.hour = hour % 24;
        this.minute = minute % 60;
        this.second = second % 60;
    }

    public Date(Date dateCurrent /* new */, Date dateCompared /* old */) {

        int diffYear, diffMonth, diffDay, diffHour, diffMinute, diffSecond;

        if (dateCurrent != null && dateCompared != null) {

            // Year
            diffYear = (dateCurrent.getYear() >= dateCompared.getYear()) ?
                    dateCurrent.getYear() - dateCompared.getYear() : 0;

            // Month
            diffMonth = (dateCurrent.getMonth() >= dateCompared.getMonth()) ?
                    dateCurrent.getMonth() - dateCompared.getMonth() : 0;

            // Day
            diffDay = (dateCurrent.getDay() >= dateCompared.getDay()) ?
                    dateCurrent.getDay() - dateCompared.getDay() : 0;

            // Hour
            diffHour = (dateCurrent.getHour() >= dateCompared.getHour()) ?
                    dateCurrent.getHour() - dateCompared.getHour() : 0;

            // Minute
            diffMinute = (dateCurrent.getMinute() >= dateCompared.getMinute()) ?
                    dateCurrent.getMinute() - dateCompared.getMinute() : 0;

            // Second
            diffSecond = (dateCurrent.getSecond() >= dateCompared.getSecond()) ?
                    dateCurrent.getMinute() - dateCompared.getSecond() : 0;

        } else {

            diffYear = 0;
            diffMonth = 0;
            diffDay = 0;
            diffHour = 0;
            diffMinute = 0;
            diffSecond = 0;

        }

        this.year = diffYear;
        this.month = ((diffMonth - 1) % 12) + 1;
        this.day = ((diffDay - 1) % 31) + 1;
        this.hour = diffHour % 24;
        this.minute = diffMinute % 60;
        this.second = diffSecond % 60;
    }

    //region Getters

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    //endregion

    //region Setters

    public void setYear(int year) {
        if (year < 1000) { year = 1000; }
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month % 12;
    }

    public void setDay(int day) {
        this.day = day % 31;
    }

    public void setHour(int hour) {
        this.hour = hour % 24;
    }

    public void setMinute(int minute) {
        this.minute = minute % 60;
    }

    public void setSecond(int second) {
        this.second = second % 60;
    }

    //endregion

    public boolean isInFuture() {
        LocalDate currentDate = LocalDate.now();
        LocalDate givenDate = LocalDate.of(this.year, this.month, this.day);
        if (givenDate.isBefore(currentDate) || givenDate.isEqual(currentDate)) {
            return false;
        } else {
            return true;
        }
    }

    // @Override
    public String toString(boolean full) {

        String strSecond, strMinute, strHour, strDay, strMonth, strYear;
        strSecond = (this.second < 10) ? "0" + this.second : "" + this.second;
        strMinute = (this.minute < 10) ? "0" + this.minute : "" + this.minute;
        strHour = "" + this.hour;
        strDay = "" + this.day;
        strMonth = "" + this.month;
        strYear = "" + this.year;

        if (full) {
            return (
                    strDay + "." + strMonth + "." + strYear + ", " +
                            strHour + ":" + strMinute + ":" + strSecond
            );
        } else {
            return (
                    strDay + "." + strMonth + "." + strYear
            );
        }

    }
}
