package com.cookandroid.smartmirror.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

import com.cookandroid.smartmirror.Methods;

import java.time.LocalDateTime;

public class scheduleData implements Parcelable {
    private String title;
    private int iconRes;
//    private String time;
    // time을 "0000년 00월 00일 00시 00분"으로 입력한다고 가정.
    private String startTime;   // 0000-00-00 00:00:00
    private String endTime;     // 0000-00-00 00:00:00
    private String date;        // 0000-00-00
    private String startHour, startMinute, endHour, endMinute;
//    private int startMinute;
//    private int endHour;
//    private int endMinute;
    private int schedule_id;
    private int user_num;

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    protected scheduleData(Parcel in) {
        date = in.readString();
        title = in.readString();
        iconRes = in.readInt();
        startTime = in.readString();
        endTime = in.readString();
        startHour = in.readString();
        startMinute = in.readString();
        endHour = in.readString();
        endMinute = in.readString();
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
//        return "제목: "+title+", 시작시간: "+startHour+"시 "+startMinute+"분 - "+endHour+"시 "+endMinute+"분 입니다.";
        return "아이디: "+schedule_id+", 제목: "+title+", 시작시간: "+startTime+", 종료시간: "+endTime+" 입니다.";
    }

    public scheduleData(int schedule_id, int user_num, String start_time, String end_time, String title){
        this.schedule_id = schedule_id;
        this.user_num = user_num;
        this.startTime = start_time;
        this.endTime = end_time;
        this.title = title;
        this.date = Methods.getDateFromDateString(start_time);
        this.startHour=Methods.getHourFromDateString(start_time);
        this.startMinute = Methods.getMinuteFromDateString(start_time);
        this.endHour=Methods.getHourFromDateString(end_time);
        this.endMinute = Methods.getMinuteFromDateString(end_time);

        // 0000-00-00 00:00:00에서 가져와야 합니다.

    }
    public String getTitle(){
        return title;
    }
    public int getIconRes(){
        return iconRes;
    }
    public String getStartTime(){return startTime;}
    public String getStartHour(){return startHour;}
    public String getStartMinute() {return startMinute;}
    public String getEndHour() { return endHour;}
    public String getEndMinute() {return endMinute;}
    public int getSchedule_id() { return schedule_id; }
    public int getUser_num() { return user_num; }

    public String getDate() { return date; }

    public String getEndTime(){return endTime;}
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
        dest.writeString(startHour);
        dest.writeString(startMinute);
        dest.writeString(endHour);
        dest.writeString(endMinute);
    }
//    public String getDetail(){
//        return detail;
//    }
}
