package com.grid.cuiletian.library;

/**
 * Created by cuiletian on 15/10/16.
 */
public class Day {
    private String date; // yyyy-mm-dd格式，可用来排序
    private String dateDisplay; // 用来展示，例如"7-29", "今天", "圣诞节"
    private String dayOfWeek;
    private boolean isHoliday;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateDisplay() {
        return dateDisplay;
    }

    public void setDateDisplay(String dateDisplay) {
        this.dateDisplay = dateDisplay;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

}