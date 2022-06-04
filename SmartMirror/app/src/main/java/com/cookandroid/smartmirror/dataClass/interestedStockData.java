package com.cookandroid.smartmirror.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

public class interestedStockData implements Parcelable {
    private int stock_id, user_num;
    private String stock_name, stock_code;
    public interestedStockData(int stock_id, int user_num, String stock_name, String stock_code){
        this.stock_id = stock_id;
        this.user_num = user_num;
        this.stock_name = stock_name;
        this.stock_code = stock_code;
    }

    protected interestedStockData(Parcel in) {
        stock_id = in.readInt();
        user_num = in.readInt();
        stock_name = in.readString();
        stock_code = in.readString();
    }

    public static final Creator<interestedStockData> CREATOR = new Creator<interestedStockData>() {
        @Override
        public interestedStockData createFromParcel(Parcel in) {
            return new interestedStockData(in);
        }

        @Override
        public interestedStockData[] newArray(int size) {
            return new interestedStockData[size];
        }
    };

    public String toString(){
        return "stock_name: "+stock_name+", stock_code: "+stock_code;
    }
    public int getStock_id() { return stock_id; }

    public String getStock_code() { return stock_code; }

    public int getUser_num() { return user_num; }

    public String getStock_name() { return stock_name; }

    public void setStock_id(int stock_id) { this.stock_id = stock_id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stock_id);
        dest.writeInt(user_num);
        dest.writeString(stock_name);
        dest.writeString(stock_code);
    }
}
