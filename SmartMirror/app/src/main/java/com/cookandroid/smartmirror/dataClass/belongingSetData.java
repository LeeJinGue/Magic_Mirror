package com.cookandroid.smartmirror.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

import com.cookandroid.smartmirror.Methods;

import java.util.ArrayList;

public class belongingSetData implements Parcelable {
    private int belonging_id; // 소지품세트 아이디
    private int user_num;       // 소지품세트를 보유한 유저의 아이디
    private String set_name, activation, set_info; // 소지품 세트 이름, 상세설명
    private String stuff_list_str; // stuff_list(String 형태)
    private boolean isActiavted; // 소지품세트 활성화 여부

    public belongingSetData(int belonging_id, int user_num, String set_name, String activation, String set_info, String stuff_list_str){
        this.belonging_id = belonging_id;
        this.user_num = user_num;
        this.set_name = set_name;
        this.set_info = set_info;
        this.activation = activation;
        if(activation.equals("0")){
            this.isActiavted = false;
        }else{
            this.isActiavted = true;
        }
        this.stuff_list_str = stuff_list_str;
    }


    protected belongingSetData(Parcel in) {
        belonging_id = in.readInt();
        user_num = in.readInt();
        set_name = in.readString();
        activation = in.readString();
        set_info = in.readString();
        stuff_list_str = in.readString();
        isActiavted = in.readByte() != 0;
    }

    public static final Creator<belongingSetData> CREATOR = new Creator<belongingSetData>() {
        @Override
        public belongingSetData createFromParcel(Parcel in) {
            return new belongingSetData(in);
        }

        @Override
        public belongingSetData[] newArray(int size) {
            return new belongingSetData[size];
        }
    };

    public String toString(){
        return set_name+" 소지품세트 정보\n"+"ID: "+belonging_id+"\n상세설명: "+set_info+"\n소지품 리스트: "+stuff_list_str+"\n활성화여부: "+activation;
    }

    public String getStuff_list_str() { return stuff_list_str; }
    public int getUser_num() { return user_num; }
    public int getBelonging_id() { return belonging_id; }
    public boolean isActiavted() { return isActiavted; }
    public String getSet_info() { return set_info; }
    public String getSet_name() { return set_name; }
    public String getActivation(){return activation;}


    public void setActiavted(boolean isActiavted){
        this.isActiavted = isActiavted;
        if(isActiavted){
            this.activation = "1";
        }else{
            this.activation = "0";
        }
    }
    public void setActivation(String activation) {
        this.activation = activation;
        if(activation.equals("0")){
            this.isActiavted = false;
        }else{
            this.isActiavted = true;
        }
    }

    public void setBelonging_id(int belonging_id) { this.belonging_id = belonging_id; }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public void setSet_name(String set_name){this.set_name = set_name;}
    public void setSet_info(String set_info){this.set_info = set_info;}
    public void setStuff_list_str(String newStuffList_str){this.stuff_list_str = newStuffList_str;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(belonging_id);
        dest.writeInt(user_num);
        dest.writeString(set_name);
        dest.writeString(activation);
        dest.writeString(set_info);
        dest.writeString(stuff_list_str);
        dest.writeByte((byte) (isActiavted ? 1 : 0));
    }


}
