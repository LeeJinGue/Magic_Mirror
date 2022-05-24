package com.cookandroid.smartmirror.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.custom.customEditText;
import com.cookandroid.smartmirror.MirrorDBHelper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.*;

public class PutDevWifiAddress extends AppCompatActivity {
    customEditText editIpAddress;
    TextView tvTitle, putIpAddress;
    Button linkDevBtn;
    Toolbar toolbar;

    LinearLayout mainLayout;
    InputMethodManager manager;
    MirrorDBHelper sqlDB;

    private static final int port = 8080;
    private static final String dongIp = "192.168.43.180";
    private static final String myIp = "192.168.0.6";
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
                editIpAddress.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_dev_wifi_address);
        // Add Coustom AppBar & Set Title Color Gradient
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);

        putIpAddress = findViewById(R.id.putIpAddress);
        linkDevBtn = findViewById(R.id.linkDevBtn);
        mainLayout = findViewById(R.id.registerMainLayout);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        editIpAddress = findViewById(R.id.editIpAddress);
        sqlDB = new MirrorDBHelper(getApplicationContext(), 1);

        linkDevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = editIpAddress.getText().toString();
//                ConnectThread connectThread = new ConnectThread(myIp);
//                connectThread.start();

                Intent intent = new Intent(getApplicationContext(), RegisterDevActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editIpAddress.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_ENTER){
                        editIpAddress.clearFocus();
                        manager.hideSoftInputFromWindow(editIpAddress.getWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });
    }
    class ConnectThread extends Thread{
        String hostname;

        public ConnectThread(String addr){
            hostname = addr;
        }
        public void run(){

            try {
                Log.i("PutDevWifiAddress", hostname+"로 접속을 시도합니다.");
                Socket sock = new Socket(hostname, port);
                Log.i("PutDevWifiAddress", "서버 접속");
                ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream());
                outputStream.writeChars("안녕하세요");
                outputStream.flush();
                Log.i("PutDevWifiAddress", "서버로 보낼 메시지 : " + "안녕하세요");
                sock.close();
            }catch (UnknownHostException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "연결오류. IP주소를 다시 입력해주세요.",Toast.LENGTH_SHORT);
                Log.i("PutDevWifiAddress", "호스트의 IP주소 식별불가");
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "네트워크 응답이 없습니다. 다시 시도해주세요.",Toast.LENGTH_SHORT);
                Log.i("PutDevWifiAddress", "네트워크응답없음");
            }catch (SecurityException e){
                e.printStackTrace();
                Log.i("PutDevWifiAddress", "보안에러");
            }catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.i("PutDevWifiAddress", "포트번호 오버됨");
            }

        }
    }
}