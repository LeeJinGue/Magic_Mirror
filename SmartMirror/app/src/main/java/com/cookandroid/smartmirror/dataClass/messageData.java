package com.cookandroid.smartmirror.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class messageData implements Parcelable {
    private int message_id, user_num, sender_num, hour, minute;
    private String text;
    private String date;
    public messageData(int message_id, int user_num, int sender_num, String text, String date, int hour, int minute){
        this.date = date;
        this.message_id = message_id;
        this.user_num = user_num;
        this.sender_num = sender_num;
        this.text = text;
        this.hour = hour;
        this.minute = minute;

    }

    protected messageData(Parcel in) {
        message_id = in.readInt();
        user_num = in.readInt();
        sender_num = in.readInt();
        text = in.readString();
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

    public String toString(){
        return "보낸사람: "+sender_num+", 받는사람: "+sender_num+", 메시지 내용: "+text+", 날짜: "+date.toString()+", 시간: "+hour+"시 "+minute+"분";
    }
    public String getDate() {
        return date;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(message_id);
        dest.writeInt(user_num);
        dest.writeInt(sender_num);
        dest.writeString(text);
    }
}