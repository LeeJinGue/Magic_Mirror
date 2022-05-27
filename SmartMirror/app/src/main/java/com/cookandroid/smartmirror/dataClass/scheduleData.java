package com.cookandroid.smartmirror.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

public class scheduleData implements Parcelable {
    private String title;
    private int iconRes;
//    private String time;
    private String startTime;
    private String endTime;
    private String date;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int schedule_id;

    protected scheduleData(Parcel in) {
        date = in.readString();
        title = in.readString();
        iconRes = in.readInt();
        startTime = in.readString();
        endTime = in.readString();
        startHour = in.readInt();
        startMinute = in.readInt();
        endHour = in.readInt();
        endMinute = in.readInt();
        schedule_id = in.readInt();
    }

    public static final Creator<scheduleData> CREATOR = new Creator<scheduleData>() {
        @Override
        public scheduleData createFromParcel(Parcel in) {
            return new scheduleData(in);
        }

        @Override
        public scheduleData[] newArray(int size) {
            return new scheduleData[size];
        }
    };

    public String toString(){
        return "제목: "+title+", 시작시간: "+startHour+"시 "+startMinute+"분 - "+endHour+"시 "+endMinute+"분 입니다.";
    }

    //    private String detail;
    public scheduleData(int schedule_id,String date, String title, int iconRes, int startHour, int startMinute, int endHour, int endMinute){
        this.schedule_id = schedule_id;
        this.date = date;
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
    public void setIconRes(int iconRes){this.iconRes = iconRes;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeInt(iconRes);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeInt(startHour);
        dest.writeInt(startMinute);
        dest.writeInt(endHour);
        dest.writeInt(endMinute);
    }
//    public String getDetail(){
//        return detail;
//    }
}
