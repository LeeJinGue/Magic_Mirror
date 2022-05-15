package com.cookandroid.smartmirror.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class belongingSetData implements Parcelable {
    // 소지품세트 이름
    private String name;
    // 소지품 리스트
    private ArrayList<String> belongingList;
    // 소지품 세트 상세설명
    private String detail;
    // 소지품 세트가 선택(활성화) 되어있는지
    private boolean isSelected;
    public belongingSetData(String name, String detail, ArrayList<String> belongingList){
        this.name = name;
        this.detail = detail;
        this.belongingList = belongingList;
        this.isSelected = false;
    }
    public String toString(){
        return "이름: "+name+"\n상세설명: "+detail+"\n소지품 리스트: "+belongingList.toString()+"\n선택여부: "+isSelected;
    }
    protected belongingSetData(Parcel in) {
        name = in.readString();
        belongingList = in.createStringArrayList();
        detail = in.readString();
        isSelected = in.readByte() != 0;
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

    public ArrayList<String> getBelongingList() {
        return belongingList;
    }

    public String getDetail() {
        return detail;
    }
    public boolean getSelected(){
        return isSelected;
    }
    public String getName() {
        return name;
    }
    public void setSelected(boolean selected){
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeStringList(belongingList);
        dest.writeString(detail);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
