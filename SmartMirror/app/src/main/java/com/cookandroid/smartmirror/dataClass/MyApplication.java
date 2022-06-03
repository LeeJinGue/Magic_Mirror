
package com.cookandroid.smartmirror.dataClass;

import android.app.Application;
import android.util.Log;

import com.cookandroid.smartmirror.Methods;

import java.util.ArrayList;

// 공유 데이터 관리
public class MyApplication extends Application {
    ArrayList<String> profileNameList = new ArrayList<>();
    private String selectedProfileName = null;
    private String testData = "First";
    public String getTestData(){return testData;}
    private userData selectedUser = null;
    public void setTestData(String testData){
        this.testData = testData;
    }
    private String IPString, PortString, SerialString;
    public void setAddress(String IPString){
        this.IPString=IPString;
        this.PortString= Methods.DEFAULT_PORT;
    }
    public void setSerialString(String serialString) { SerialString = serialString; }
    public String getIPString(){return IPString;}
    public String getPortString(){return PortString;}
    public String getSerialString(){return SerialString;}

    public userData getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(userData selectedUser){
        this.selectedUser = selectedUser;
        setId(selectedUser.getUser_num());
    }
    public void setId(int id){
        msgId = id*1000;
        profileId = id*100;
        scheduleId = id * 1000;
        belongingId = id * 1000;
        stockId = id * 100;
        Log.i("MyApplication", "id setting - profileId: "+profileId+", msgId: "+msgId+", scheduleId: "+scheduleId+", belongingId: "+belongingId+", stockId: "+stockId);
    }
    public int msgId = 1;
    public int stockId = 1;
    public int profileId = 1;
    public int scheduleId = 1;
    public int belongingId = 1;
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
