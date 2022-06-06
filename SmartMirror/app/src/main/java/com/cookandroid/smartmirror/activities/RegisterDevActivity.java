package com.cookandroid.smartmirror.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.smartmirror.helper.MethodsHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.custom.customEditText;
import com.cookandroid.smartmirror.helper.MirrorDBHelper;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.devData;

import org.json.JSONObject;

import java.io.FileInputStream;

public class RegisterDevActivity extends AppCompatActivity {
    customEditText editSerial, editIP;
    TextView tvTitle, editTest;
    Button btn;
    LinearLayout mainLayout;
    InputMethodManager manager;
    Toolbar toolbar;
    MirrorDBHelper sqlDB;
    AssetManager assetManager;
    JSONObject obj;
    Handler handler = new Handler();
    devData nowDevData;

    private static final String dongIp = "192.168.43.180";
    private static final String myIp = "192.168.0.6";


    public void checkIPandSerial(String IPAddress, String SerialNo){

    }
    // When touch anything, EditText focus out and keyboard down
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if(view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")){
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if(x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()){
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
                editSerial.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public FileInputStream readExcelFileFromAssets() {
        try {
            // initialize asset manager
            AssetManager assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("stock_list.xlsx");
            FileInputStream fileInputStream = fileDescriptor.createInputStream();
            return fileInputStream;
//            ExcelHelper excelHelper = new ExcelHelper(fileInputStream);
//            ArrayList<stockData> stockList = excelHelper.readStockExcelFile();
//            sqlDB.addStockList(stockList);

        } catch (Exception e) {
            Log.e("readExcelFileFromAssets", "error "+ e.toString());
        }
        return null;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(nowDevData!=null){
//            // 받아온 데이터가 있으면 서버에 Serial넘버가 맞는지 확인합니다.
//            Log.i("nowDevData", "DB에 있던 디바이스 정보: "+nowDevData.toString());
//            if(sqlDB.getNetworkHelper().checkSerialFromServer(nowDevData)){
//                // 맞으면 다음 화면으로 넘어갑니다.
//                Log.i("RegisterDevActivity", "IP주소, 시리얼넘버가 일치합니다.");
//                // 연결되었으므로 DB를 초기화
//                sqlDB.addAllTable();
//                MyApplication myApp = (MyApplication) getApplicationContext();
//                myApp.setAddress(nowDevData.getIp());
//                Intent intent = new Intent(getApplicationContext(), ProfileSelectActivity.class);
//                startActivity(intent);
//            }
//        }
//    }



    //    public boolean checkSerialIPFromServer(String serialNo, String IPAddress){
//        try{
//            // DB에서 devData를 가져옵니다.
//            devData DBDevData = sqlDB.getDBDevData();
//
//            if(sqlDB.getNetworkHelper().checkSerialFromServer(DBDevData)){
//                Log.i("RegisterDevActivity", "IP주소, 시리얼넘버가 일치합니다.");
//                // 연결되었으므로 DB를 초기화
//                sqlDB.addAllTable();
//                MyApplication myApp = (MyApplication) getApplicationContext();
//                myApp.setAddress(IPAddress);
////                        sqlDB.initDB();
//                Intent intent = new Intent(getApplicationContext(), ProfileSelectActivity.class);
//                startActivity(intent);
//
//            }else{
//                Log.i("RegisterDevActivity", "IP주소, 시리얼넘버가 일치하지 않습니다.");
//                Toast.makeText(getApplicationContext(), "시리얼넘버 또는 아이피주소를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
//            }
//
//        }catch (NumberFormatException e){
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "시리얼넘버를 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dev);

        // Add Coustom AppBar & Set Title Color Gradient
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.toolbarTv);
        MethodsHelper methodsHelper = new MethodsHelper();
        methodsHelper.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);

        sqlDB = new MirrorDBHelper(getApplicationContext(), 1);
        sqlDB.initDBbeforeLogin(readExcelFileFromAssets());
        nowDevData = sqlDB.getDBDevData();
        editSerial = findViewById(R.id.editSerial);
        editIP = findViewById(R.id.editWifi);
        btn = findViewById(R.id.testBtnRegister);
        mainLayout = findViewById(R.id.registerMainLayout);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        editTest = findViewById(R.id.editTest);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력받은 시리얼넘버, 아이피주소로 로그인
                // 테스트값은 시리얼넘버1, 아이피주소1
                try{
                    String serialNo = editSerial.getText().toString();
                    String IPAddress = editIP.getText().toString();
                    if(!MethodsHelper.isInteger(serialNo)){
                        Toast.makeText(getApplicationContext(), "시리얼넘버를 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!MethodsHelper.isIP(IPAddress)){
                        Toast.makeText(getApplicationContext(), "아이피 주소를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    nowDevData = new devData(serialNo, IPAddress);

                    // IP주소, 시리얼넘버가 맞는지 확인합니다.
                    if(sqlDB.getNetworkHelper().checkSerialFromServer(nowDevData)){
                        sqlDB.updateDevData(nowDevData);
                        sqlDB.getNetworkHelper().setIPString(nowDevData.getIp());
                        Log.i("RegisterDevActivity", "IP주소, 시리얼넘버가 일치합니다.");
                        // 연결되었으므로 DB를 초기화
                        sqlDB.addAllTable();
                        MyApplication myApp = (MyApplication) getApplicationContext();
                        myApp.setAddress(nowDevData.getIp());
//                        sqlDB.initDB();
                        Intent intent = new Intent(getApplicationContext(), ProfileSelectActivity.class);
                        startActivity(intent);

                    }else{
                        Log.i("RegisterDevActivity", "IP주소, 시리얼넘버가 일치하지 않습니다.");
                        Toast.makeText(getApplicationContext(), "시리얼넘버 또는 아이피주소를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    }

                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "시리얼넘버를 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editSerial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_ENTER){
                        editSerial.clearFocus();
                        manager.hideSoftInputFromWindow(editSerial.getWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });
        editIP.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_ENTER){
                        editIP.clearFocus();
                        manager.hideSoftInputFromWindow(editIP.getWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });
        if(nowDevData!=null){
            editIP.setText(nowDevData.getIp());
            editSerial.setText(nowDevData.getSerial_no());
        }
        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

}