package com.cookandroid.smartmirror.dataClass;

public class scheduleData {
    private String title;
    private int iconRes;
//    private String time;
    private String startTime;
    private String endTime;

//    private String detail;
    public scheduleData(String title, int iconRes, String startTime, String endTime){
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.iconRes = iconRes;
//        this.detail = detail;
    }
    public String getTitle(){
        return title;
    }
    public int getIconRes(){
        return iconRes;
    }
    public String getStartTime(){
        return startTime;
    }
    public String getEndTiem(){return endTime;}
//    public String getDetail(){
//        return detail;
//    }
}
