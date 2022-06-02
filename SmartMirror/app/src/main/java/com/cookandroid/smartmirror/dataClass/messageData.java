package com.cookandroid.smartmirror.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;

import java.time.LocalDateTime;
import java.util.Date;

public class messageData implements Parcelable {
    private int message_id, user_num, sender_num, year, month, day, hour, minute;
    private String text, date;
//    private String date;
    private LocalDateTime localDateTime;
    private int viewType;

    protected messageData(Parcel in) {
        message_id = in.readInt();
        user_num = in.readInt();
        sender_num = in.readInt();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        text = in.readString();
        date = in.readString();
        viewType = in.readInt();
    }

    public static final Creator<messageData> CREATOR = new Creator<messageData>() {
        @Override
        public messageData createFromParcel(Parcel in) {
            return new messageData(in);
        }

        @Override
        public messageData[] newArray(int size) {
            return new messageData[size];
        }
    };

    public void setMessage_id(int id){
        this.message_id = id;
    }
    public static final int TYPE_DATE = 0;
    public static final int TYPE_MESSAGE = 1;
    // 일반 메세지. 받은기준?
    public messageData(int message_id, int user_num, int sender_num, String text, String date, boolean isReceived){
        this.message_id = message_id;
        this.user_num = user_num;
        this.sender_num = sender_num;
        this.text = text;
        this.date = date;
        this.year = Integer.parseInt(Methods.getYearFromDateString(date));
        this.month = Integer.parseInt(Methods.getMonthFromDateString(date));
        this.day = Integer.parseInt(Methods.getDayFromDateString(date));
        this.hour = Integer.parseInt(Methods.getHourFromDateString(date));
        this.minute = Integer.parseInt(Methods.getMonthFromDateString(date));
        if(isReceived){ this.viewType = R.integer.TYPE_MESSAGE_LEFT;
        }else{ this.viewType = R.integer.TYPE_MESSAGE_RIGHT; }
        this.localDateTime = LocalDateTime.of(year, month, day, hour, minute);
    }
    // 날짜용
    public messageData(String datetTime){
        this.year = Integer.parseInt(Methods.getYearFromDateString(datetTime));
        this.month = Integer.parseInt(Methods.getMonthFromDateString(datetTime));
        this.day = Integer.parseInt(Methods.getDayFromDateString(datetTime));
        this.viewType = R.integer.TYPE_DATE;
    }





    public String toString(){
        if(viewType != R.integer.TYPE_DATE){
            return "보낸사람: "+sender_num+", 받는사람: "+user_num+", 메시지 내용: "+text+", 날짜: "+year+"년 "+month+"월 "+date+"일 "+hour+"시 "+minute+"분"+", LocalDateTime: "+localDateTime.toString();
        }else{
            return "날짜: "+year+"년 "+month+"월 "+date+"일";

        }
    }
    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() {
        return day;
    }
    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }
    public int getMessage_id() {
        return message_id;
    }
    public int getSender_num() {
        return sender_num;
    }
    public int getUser_num() {
        return user_num;
    }
    public String getText() {
        return text;
    }
    public int getViewType() {
        return viewType;
    }
    public String getDate(){return date;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(message_id);
        dest.writeInt(user_num);
        dest.writeInt(sender_num);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeString(text);
        dest.writeString(date);
        dest.writeInt(viewType);
    }
}