package com.cookandroid.smartmirror;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cookandroid.smartmirror.dataClass.devData;
import com.cookandroid.smartmirror.dataClass.userData;

import java.util.ArrayList;

public class MirrorDBHelper extends SQLiteOpenHelper {
    private int dbVersion;
    private SQLiteDatabase db;
    private MirrorNetworkHelper networkHelper;
    public MirrorDBHelper(@Nullable Context context, int version) {
        super(context, "groupDB", null, 1);
        dbVersion = version;
        db = getWritableDatabase();
        networkHelper = new MirrorNetworkHelper();
        onCreate(db);
    }

    public void initDB(){
        Log.i("DataBase", "DB초기화");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS groupTBL ( gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        createDeviceTb(db);
        createUserTb(db);
        createLayoutSettingTb(db);
        createMessageTb(db);
        createScheduleTb(db);
        createStockTb(db);
        createBelongingsTb(db);
        // dev데이터를 미리 넣어둡니다.
        addDevData(networkHelper.getDevData());
        // userData를 미리 넣어둡니다.
        addUser(networkHelper.getUserData());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
    }
    public void createDeviceTb(SQLiteDatabase db){
        // device 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS device");
        db.execSQL("CREATE TABLE IF NOT EXISTS device " +
                "(serial_no INTEGER(11) PRIMARY KEY, " +
                "ip VARCHAR(20) DEFAULT NULL, " +
                "port INTEGER(11) DEFAULT NULL, " +
                "location VARCHAR(255) DEFAULT NULL, " +
                "info VARCHAR(255) DEFAULT NULL);");
        Log.i("Create Table", "device table 생성 완료");
        // Device정보 초기값(IP주소, 시리얼넘버) 넣어놓기

    }
    public void createUserTb(SQLiteDatabase db){
        // user 테이블 생성
        // 0: 유저아이디, 1: 시리얼넘버, 2: 이름, 3: 유저 이미지 경로
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("CREATE TABLE IF NOT EXISTS user " +
                "(user_num INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "serial_no INTEGER(11), " +
                "name VARCHAR(100) NOT NULL, " +
                "user_image_pass VARCHAR(255) NOT NULL" +
//                ", FOREIGN KEY(serial_no) " +
//                "REFERENCES device(serial_no)" +
                        ");");
        Log.i("Create Table", "user table 생성 완료");
    }
    public void createLayoutSettingTb(SQLiteDatabase db){
        // layoutsetting 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS layoutsetting");
        db.execSQL("CREATE TABLE IF NOT EXISTS layoutsetting " +
                "(layout_id INTEGER(11) PRIMARY KEY, " +
                "user_no INTEGER(11) NOT NULL, " +
                "type INTEGER(11) NOT NULL, " +
                "loc INTEGER(11) NOT NULL" +
//                ", FOREIGN KEY(user_no)" +
//                "REFERENCES user(user_no)" +
                ");");
        Log.i("Create Table", "layout setting table 생성 완료");
    }
    public void createBelongingsTb(SQLiteDatabase db){
        // belongings 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS belongings");
        db.execSQL("CREATE TABLE IF NOT EXISTS belongings " +
                "(belonging_id INT(11) PRIMARY KEY, " +
                "set_name VARCHAR(255) NOT NULL, " +
                "user_no INTEGER(11), " +
                "stuff_list TEXT(255)" +
//                ", FOREIGN KEY(user_no)" +
//                "REFERENCES user(user_no)" +
                ");");
        Log.i("Create Table", "belongings table 생성 완료");
    }
    public void createMessageTb(SQLiteDatabase db){
        // message 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS message");
        db.execSQL("CREATE TABLE IF NOT EXISTS message " +
                "(message_id INTEGER(11) PRIMARY KEY, " +
                "receiver_num INTEGER(11), " +
                "sender_num INTEGER(11), " +
                "text VARCHAR(100) DEFAULT NULL, " +
                "data DATETIME DEFAULT NULL" +
//                ", FOREIGN KEY(receiver_num)" +
//                "REFERENCES user(user_no)," +
//                "FOREIGN KEY(sender_num)" +
//                "REFERENCES user(user_no)" +
                ");");
        Log.i("Create Table", "message table 생성 완료");
    }
    public void createScheduleTb(SQLiteDatabase db){
        // schedule 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS schedule");
        db.execSQL("CREATE TABLE IF NOT EXISTS schedule " +
                "(schedule_id INTEGER(11) PRIMARY KEY, " +
                "user_no INTEGER(11), " +
                "start_time DATETIME DEFAULT NULL, " +
                "end_time DATETIME DEFAULT NULL, " +
                "text VARCHAR(255) DEFAULT NULL" +
//                ", FOREIGN KEY(user_no)" +
//                "REFERENCES user(user_no)" +
                ");");
        Log.i("Create Table", "schedule table 생성 완료");
    }
    public void createStockTb(SQLiteDatabase db){
        // stock 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS stock");
        db.execSQL("CREATE TABLE IF NOT EXISTS stock " +
                "(stock_id INTEGER(11) PRIMARY KEY, " +
                "user_no INTEGER(11), " +
                "stock_name VARCHAR(255) NOT NULL, " +
                "stock_code VARCHAR(50)" +
//                ", FOREIGN KEY(user_no)" +
//                "REFERENCES user(user_no)" +
                ");");
        Log.i("Create Table", "stock table 생성 완료");
    }
    // -----------------------User관련--------------------------------
    public ArrayList<userData> getAllUserList(){
        Cursor mCursor = db.rawQuery("SELECT * FROM user", null);
        ArrayList<userData> list = new ArrayList<userData>();
        if(mCursor.moveToFirst()){
            do {
                int id = mCursor.getInt(0);
                int serial_no = mCursor.getInt(1);
                String name = mCursor.getString(2);
                String usr_img_path = mCursor.getString(3);
//                Log.i("getUserData", "id: "+id+", serial_no: "+serial_no+", name: "+name+", user_image_path: "+usr_img_path+" 유저데이터를 가져옵니다.");
                list.add(new userData(id, serial_no, name, usr_img_path));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        for(userData u:list) Log.i("getUserData", "id: "+u.getUser_num()+", serial_no: "+u.getSerial_no()+", name: "+u.getName()+", user_image_path: "+u.getUser_image_pass()+" 유저데이터를 가져옵니다.");
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    // user추가
    public void addUser(userData newUser){
        db.execSQL("INSERT INTO user VALUES(" +
                newUser.getUser_num() +
                ", "+ newUser.getSerial_no()+
                ",'"+ newUser.getName()+
                "','"+ newUser.getUser_image_pass()+
                "');");
        Log.i("addUserData", "신규유저 "+newUser.toString()+" 추가");
    }

    // user 수정
    public void editUserName(userData editUser){
        db.execSQL("UPDATE user SET name = '"+ editUser.getName() + "'"
        + "WHERE user_num = "+editUser.getUser_num());
        Log.i("editUserNameAt", " "+editUser.toString()+"로 수정");

    }

    // ---------------------------------------------------------------

    // --------------------------Device 관련-------------------------
    // IP주소, 시리얼넘버가 맞는지 체크
    public boolean checkIPAddressAndSerial(String IPAddress, int Serial){
        // DB에 있는 Data 받아올 변수들
        int db_serial = -1;
        int db_port = -1;
        String db_ip = "";

        // 시리얼번호, IP, Port데이터 갖고옵니다.
        Cursor cs = db.rawQuery("SELECT serial_no, ip, port FROM device", null);

        // Cursor객체를 통해 DB에 저장된 데이터를 읽어옵니다.
        while(cs.moveToNext()){
            db_serial = cs.getInt(0);
            db_ip = cs.getString(1);
            db_port = cs.getInt(2);
        }
        // 받아온 ip가 ""라면 -> DB에 저장된게 없다면 추가해야하는데?
        if(db_ip ==""){
            Log.i("DBHelper", "checkIPAddressAndSerial - 저장된 IP / Serial 정보가 없습니다.");
            return false;
        }
        if(db_ip.equals(IPAddress)){
            if(db_serial == Serial){
                // IP주소가 같고 시리얼넘버도 같다면
                return true;
            }else{
                // IP주소는 같지만 serial넘버가 다르다.
                Log.i("DBHelper", "checkIPAddressAndSerial - Serial 정보가 일치하지 않습니다.");
                return false;
            }
        }
        // IP주소가 다르다.
        Log.i("DBHelper", "checkIPAddressAndSerial - IP주소가 일치하지 않습니다.");
        return false;
    }

    // 새로운 IP주소, 시리얼넘버 정보를 추가
    public void addDevData(devData devData){
        // 시리얼넘버, IP주소, 포트넘버, location, info순
        db.execSQL("INSERT INTO device VALUES(" +
                devData.getSerial_no() +
                ", '" + devData.getIp() +
                "', " + devData.getPort() +
                ", '" + devData.getLocation() +
                "', '" + devData.getInfo()+
                "');");
        Log.i("addDevData", "새 데이터 "+devData.toString()+" 추가");

    }
    // ---------------------------------------------------------------


}
