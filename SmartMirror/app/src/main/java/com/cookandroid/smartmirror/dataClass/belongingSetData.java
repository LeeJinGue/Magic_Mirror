package com.cookandroid.smartmirror.dataClass;

import java.util.ArrayList;

public class belongingSetData {
    private String name;
    private ArrayList<String> belongingList;
    private String detail;
    public belongingSetData(String name, String detail, ArrayList<String> belongingList){
        this.name = name;
        this.detail = detail;
        this.belongingList = belongingList;
    }

    public ArrayList<String> getBelongingList() {
        return belongingList;
    }

    public String getDetail() {
        return detail;
    }

    public String getName() {
        return name;
    }
}
