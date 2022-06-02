package com.cookandroid.smartmirror.dataClass;

import com.cookandroid.smartmirror.R;

public class layoutData {
    private String layoutName;
    private String locationName;
    private int imageResId;
    private int layout_id;  // PK
    private int user_num;    // FK
    private int loc;    // 0~3
    private int type;   // 0~5
    public String toString(){
        return "layout_id: "+layout_id+", layoutName: "+layoutName+", locationName: "+locationName+", user_num: "+user_num+", type: "+type+", loc: "+loc;
    }
    // type 값에 맞는 인덱스로
    // 날씨, 시계, 스케쥴, 주식, 메세지, 소지품 순서 -> imageResId
    public static int[] TYPE_RES_ID = {R.drawable.ic_weather, R.drawable.ic_watch,R.drawable.ic_belonging, R.drawable.ic_calendar, R.drawable.ic_stock, R.drawable.ic_message,R.drawable.ic_schedule_black};
    // 날씨, 시계, 일정, 관심주, 메시지, 소지품세트 순서 -> layoutName
    public static String[] LAYOUT_NAME = {"날씨", "시계","소지품세트", "일정", "관심주", "메시지", "카메라"};
    // 좌상단, 우상단, 우하단, 좌하단 순서
    private static String[] LAYOUT_LOCATION = {"LEFT_UP", "RIGHT_UP", "RIGHT_DOWN", "LEFT_DOWN"};
    public layoutData(int layout_id, int user_num, int loc, int type) {
        this.layout_id = layout_id;
        this.user_num = user_num;
        this.loc = loc;
        this.type = type;
        this.layoutName = LAYOUT_NAME[type];
        this.imageResId = TYPE_RES_ID[type];
        this.locationName = LAYOUT_LOCATION[loc];
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getLayout_id() {
        return layout_id;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public String getLocationName() {
        return locationName;
    }

    public int getuser_num() {
        return user_num;
    }

    public int getLoc() {
        return loc;
    }

    public int getType() {
        return type;
    }

    public void setLayout_id(int layout_id) { this.layout_id = layout_id; }
}