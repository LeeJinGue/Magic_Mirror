package com.cookandroid.smartmirror.dataClass;

public class scheduleData {
    private String title;
    private int iconRes;
//    private String time;
    private String startTime;
    private String endTime;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;


    //    private String detail;
    public scheduleData(String title, int iconRes, int startHour, int startMinute, int endHour, int endMinute){
        this.title = title;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;

        this.iconRes = iconRes;
//        this.detail = detail;
    }
    public String getTitle(){
        return title;
    }
    public int getIconRes(){
        return iconRes;
    }
    public String getStartTime(){return startTime;}
    public int getStartHour(){return startHour;}
    public int getStartMinute() {return startMinute;}
    public int getEndHour() { return endHour;}
    public int getEndMinute() {return endMinute;}

    public String getEndTiem(){return endTime;}
//    public String getDetail(){
//        return detail;
//    }
}
