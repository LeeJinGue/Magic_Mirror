package com.cookandroid.smartmirror.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
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
import org.json.simple.*;
import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.custom.customEditText;
import com.cookandroid.smartmirror.MirrorDBHelper;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

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
    String sdcard = Environment.getExternalStorageState()+"/AP/";

    File dir = new File(sdcard);
    private static final int port = 8080;
    private static final String dongIp = "192.168.43.180";
    private static final String myIp = "192.168.0.6";

    public String getJsonWithFileName(String fileName){
        assetManager = getResources().getAssets();
        try{
            // assets 폴더 안에 있는 JSON 파일을 열어 InputStream 객체 생성
            InputStream inputStream = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line!= null){
                buffer.append(line+"\n");
                line=reader.readLine();
            }
            String jsonData = buffer.toString();
            inputStream.close();
            isr.close();
            reader.close();
            return jsonData;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public void getJsonData(){
         assetManager = getResources().getAssets();
        try{
            String jsonData = getJsonWithFileName("test.json");
            JSONObject jsonObject = new JSONObject(jsonData);
            String name = jsonObject.getString("name");
            int id = jsonObject.getInt("id");
            int code = jsonObject.getInt("code");
            JSONObject main = jsonObject.getJSONObject("main");
            double main_temp = main.getDouble("temp");
            Log.i("jsonTest", "id: "+id+", code:"+code+", name:"+name+", temp:"+main_temp);
            JSONObject jsonObject1 = new JSONObject();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dev);

        // Add Coustom AppBar & Set Title Color Gradient
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);

        sqlDB = new MirrorDBHelper(getApplicationContext(), 1);
        sqlDB.initDB();
        
        editSerial = findViewById(R.id.editSerial);
        editIP = findViewById(R.id.editWifi);
        btn = findViewById(R.id.testBtnRegister);
        mainLayout = findViewById(R.id.registerMainLayout);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        editTest = findViewById(R.id.editTest);

//        getJsonData();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ConnectThread connectThread = new ConnectThread(dongIp);
//                connectThread.start();
//                httpMain();
                // 입력받은 시리얼넘버, 아이피주소로 로그인
                // 테스트값은 시리얼넘버1, 아이피주소1
                int serialNo = Integer.parseInt(editSerial.getText().toString());
                String IPAddress = editIP.getText().toString();
                // IP주소, 시리얼넘버가 맞는지 확인합니다.
                if(sqlDB.checkIPAddressAndSerial(IPAddress, serialNo)){
                    Log.i("RegisterDevActivity", "IP주소, 시리얼넘버가 일치합니다.");
                    Intent intent = new Intent(getApplicationContext(), ProfileSelectActivity.class);
                    startActivity(intent);

                }else{
                    Log.i("RegisterDevActivity", "IP주소, 시리얼넘버가 일치하지 않습니다.");
                    Toast.makeText(getApplicationContext(), "시리얼넘버 또는 아이피주소를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();

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
        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    class ConnectThread extends Thread{
        String hostname;

        public ConnectThread(String addr){
            hostname = addr;
        }
        public void run(){

            try {
                // JSON 데이터를 파싱해서 String형태로 가져옵니다.
                String jsonData = getJsonWithFileName("test.json");
//                JSONObject jsonObject = new JSONObject(jsonData);
                Socket sock = new Socket(myIp, 8080);
                Log.i("Connect Test", "실행");
                Log.i("Connect Test", "서버 접속");
//                ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream());
//                outputStream.writeChars("안녕하세요");
//                outputStream.flush();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(sock.getOutputStream());
                outputStreamWriter.write("안녕하십니까");
                outputStreamWriter.flush();
                Log.i("Connect Test", "서버로 보낼 메시지 : " + outputStreamWriter.getEncoding());
//                sock.close();
            }catch (UnknownHostException e){
                e.printStackTrace();
                Log.i("Connect Test", "호스트의 IP주소 식별불가");
            }catch (IOException e){
                e.printStackTrace();
                Log.i("Connect Test", "네트워크응답없음");
            }catch (SecurityException e){
                e.printStackTrace();
                Log.i("Connect Test", "보안에러");
            }catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.i("Connect Test", "포트번호 오버됨");
            }
//            }catch (JSONException e){
//                e.printStackTrace();
//                Log.i("Connect Test", "JSON 에러");
//            }



        }
    }
    class ListenThread extends Thread{
        String hostname;
        public ListenThread(String addr){
            hostname = addr;
        }
        Writer output = null;
        public void run(){
            try{
                while(true){
                    int port = 11002;
                    Socket sock = new Socket(hostname, port);
                    ObjectInputStream inputStream = new ObjectInputStream(sock.getInputStream());
                    obj = (JSONObject) inputStream.readObject();
                    final String jsonData = obj.toString();
                    Log.i("Listen Test", "서버에서 받은 메시지 : "+jsonData);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                File file = new File(dir+"/data.json");
                                output = new BufferedWriter(new FileWriter(file));
                                output.write(obj.toString());
                                output.close();;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), dir+"/data.json에 저장했습니다.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), jsonData, Toast.LENGTH_SHORT).show();
                            Log.i("Listen Test", "받은 데이터 : "+jsonData);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}