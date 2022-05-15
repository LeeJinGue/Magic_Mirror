package com.cookandroid.smartmirror;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class myDBHelper extends SQLiteOpenHelper {
    private int dbVersion;
    public myDBHelper(@Nullable Context context, int version) {
        super(context, "groupDB", null, 1);
        dbVersion = version;
    }

    public void initDB(){
        Log.i("DataBase", "DB초기화");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE groupTBL ( gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(db);
    }
}
