package com.cookandroid.smartmirror;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cookandroid.smartmirror.dataClass.userData;

import java.util.ArrayList;

public class MirrorDBHelper extends SQLiteOpenHelper {
    private int dbVersion;
    private SQLiteDatabase db;
    public MirrorDBHelper(@Nullable Context context, int version) {
        super(context, "groupDB", null, 1);
        dbVersion = version;
        db = getWritableDatabase();
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
    }
    public void createDeviceTb(SQLiteDatabase db){
        // device 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS device " +
                "(serial_no INT(11) PRIMARY KEY, " +
                "ip VARCHAR(20) DEFAULT NULL, " +
                "port INT(11) DEFAULT NULL, " +
                "location VARCHAR(255) DEFAULT NULL, " +
                "info VARCHAR(255) NOT NULL);");
        Log.i("Create Table", "device table 생성 완료");
    }
    public void createUserTb(SQLiteDatabase db){
        // user 테이블 생성
        // 0: 유저아이디, 1: 시리얼넘버, 2: 이름, 3: 유저 이미지 경로
        db.execSQL("CREATE TABLE IF NOT EXISTS user " +
                "(user_num INT(11) PRIMARY KEY, " +
                "serial_no INT(11), " +
                "name VARCHAR(100) NOT NULL, " +
                "user_image_pass VARCHAR(255) NOT NULL, " +
                "FOREIGN KEY(serial_no) " +
                "REFERENCES device(serial_no));");
        Log.i("Create Table", "user table 생성 완료");
    }
    public void createLayoutSettingTb(SQLiteDatabase db){
        // layoutsetting 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS layoutsetting " +
                "(layout_id INT(11) PRIMARY KEY, " +
                "user_no INT(11) NOT NULL, " +
                "type INT(11) NOT NULL, " +
                "loc INT(11) NOT NULL, " +
                "FOREIGN KEY(user_no)" +
                "REFERENCES user(user_no));");
        Log.i("Create Table", "layout setting table 생성 완료");
    }
    public void createBelongingsTb(SQLiteDatabase db){
        // belongings 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS belongings " +
                "(belonging_id INT(11) PRIMARY KEY, " +
                "set_name VARCHAR(255) NOT NULL, " +
                "user_no INT(11), " +
                "stuff_list TEXT(255), " +
                "FOREIGN KEY(user_no)" +
                "REFERENCES user(user_no));");
        Log.i("Create Table", "belongings table 생성 완료");
    }
    public void createMessageTb(SQLiteDatabase db){
        // message 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS message " +
                "(message_id INT(11) PRIMARY KEY, " +
                "receiver_num INT(11), " +
                "sender_num INT(11), " +
                "text VARCHAR(100) DEFAULT NULL, " +
                "data DATETIME DEFAULT NULL, " +
                "FOREIGN KEY(receiver_num)" +
                "REFERENCES user(user_no)," +
                "FOREIGN KEY(sender_num)" +
                "REFERENCES user(user_no));");
        Log.i("Create Table", "message table 생성 완료");
    }
    public void createScheduleTb(SQLiteDatabase db){
        // schedule 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS schedule " +
                "(schedule_id INT(11) PRIMARY KEY, " +
                "user_no INT(11), " +
                "start_time DATETIME DEFAULT NULL, " +
                "end_time DATETIME DEFAULT NULL, " +
                "text VARCHAR(255) DEFAULT NULL, " +
                "FOREIGN KEY(user_no)" +
                "REFERENCES user(user_no)" +
                ");");
        Log.i("Create Table", "schedule table 생성 완료");
    }
    public void createStockTb(SQLiteDatabase db){
        // stock 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS stock " +
                "(stock_id INT(11) PRIMARY KEY, " +
                "user_no INT(11), " +
                "stock_numa VARCHAR(255) NOT NULL, " +
                "FOREIGN KEY(user_no)" +
                "REFERENCES user(user_no));");
        Log.i("Create Table", "stock table 생성 완료");
    }
    public ArrayList<userData> getAllUserList(){
        Cursor mCursor = db.rawQuery("SELECT * FROM user", null);
        ArrayList<userData> list = new ArrayList<userData>();
        if(mCursor.moveToFirst()){
            do {
                int id = mCursor.getInt(0);
                int serial_no = mCursor.getInt(1);
                String name = mCursor.getString(2);
                String usr_img_path = mCursor.getString(3);
                Log.i("getUserData", "id: "+id+", serial_no: "+serial_no+", name: "+name+", user_image_path: "+usr_img_path+" 유저데이터를 가져옵니다.");
                list.add(new userData(id, serial_no, name, usr_img_path));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        return list;
    }
    public void addUser(userData newUser){
        db.execSQL("INSERT INTO user VALUES(" +
                newUser.getUser_num() +
                ", "+ newUser.getSerial_no()+
                ",'"+ newUser.getName()+
                "','"+ newUser.getUser_image_pass()+
                "');");
        Log.i("addUserData", "신규유저 "+newUser.toString()+" 추가");

    }
}
