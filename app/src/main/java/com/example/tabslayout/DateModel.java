package com.example.tabslayout;

public class DateModel {
    private String key;
    private String date;
    private long timestamp;

    public DateModel() {
    }

    public String getKey() {
        return key;
    }

    public String getYearFromDate() {
        return date.substring(0, 4);
    }

    public String getMonthFromDate() {
        return date.substring(5, 7);
    }

    public String getDayFromDate() {
        return date.substring(8, 10);
    }


    public String getFormattedDate() {
        String day = getDayFromDate();
        String monthNumber = getMonthFromDate();
        String year = getYearFromDate();
        String monthName = getMonthName(Integer.parseInt(monthNumber));
        return day + " " + monthName + " " + year;
    }

    private String getMonthName(int monthNumber) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[monthNumber - 1];
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
