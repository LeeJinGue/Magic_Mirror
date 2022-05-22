package com.cookandroid.smartmirror.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

public class userData implements Parcelable {
    private int user_num;
    private int serial_no;
    private String name;
    private String user_image_pass;
    public userData(int user_num, int serial_no, String name, String user_image_pass){
        this.user_num = user_num;
        this.serial_no = serial_no;
        this.name = name;
        this.user_image_pass = user_image_pass;
    }

    protected userData(Parcel in) {
        user_num = in.readInt();
        serial_no = in.readInt();
        name = in.readString();
        user_image_pass = in.readString();
    }

    public static final Creator<userData> CREATOR = new Creator<userData>() {
        @Override
        public userData createFromParcel(Parcel in) {
            return new userData(in);
        }

        @Override
        public userData[] newArray(int size) {
            return new userData[size];
        }
    };

    public String toString(){
        return "id: "+user_num+", serial_no: "+serial_no+", name: "+name+", user_image_pass: "+user_image_pass;
    }
    public String getName() {
        return name;
    }
    public int getUser_num() {
        return user_num;
    }
    public int getSerial_no() {
        return serial_no;
    }
    public String getUser_image_pass() {
        return user_image_pass;
    }
    public void setName(String name){this.name = name;}
    public void setUser_num(int user_num){this.user_num = user_num;}
    public void setSerial_no(int serial_no){this.serial_no = serial_no;}
    public void setUser_image_pass(String image_pass){this.user_image_pass = image_pass;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_num);
        dest.writeInt(serial_no);
        dest.writeString(name);
        dest.writeString(user_image_pass);
    }
}
