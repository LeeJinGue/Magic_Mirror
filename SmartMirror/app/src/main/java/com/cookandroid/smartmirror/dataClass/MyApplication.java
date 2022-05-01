package com.cookandroid.smartmirror.dataClass;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

// 공유 데이터 관리
public class MyApplication extends Application {
    ArrayList<String> profileNameList = new ArrayList<>();
    private String selectedProfileName = null;
    private String testData = "First";
    public String getTestData(){return testData;}
    public void setTestData(String testData){
        this.testData = testData;
    }

    public void addProfileName(String name){
        Log.i("MyApplication", name+"이라는 이름의 프로필 추가");
        this.profileNameList.add(name);
    }
    public void editProfileName(String name, int index){
        Log.i("MyApplication", index+"번째 이름을"+ name+"으로 수정");
        this.profileNameList.set(index, name);
    }
    public String getProfileName(int index){return profileNameList.get(index);}
    public ArrayList<String> getProfileNameList(){return profileNameList;}

    public void setSelectedProfileName(String name){
        Log.i("MyApplication", selectedProfileName+"의 프로필 선택");
        this.selectedProfileName = name;
    }
    public String getSelectedProfileName(){return selectedProfileName;}
}
