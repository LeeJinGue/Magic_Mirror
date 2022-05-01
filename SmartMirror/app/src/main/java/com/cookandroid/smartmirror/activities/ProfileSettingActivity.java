package com.cookandroid.smartmirror.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.MyApplication;

public class ProfileSettingActivity extends AppCompatActivity {
    EditText name;
    Button regBtn;
    String mode;
    int index;
    MyApplication app;
    EditText profileName;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        profileName = findViewById(R.id.editSerial2);
        tv = findViewById(R.id.profileSettingTextView);
        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);

        app = (MyApplication) getApplicationContext();
        Intent i = getIntent();
        mode = i.getStringExtra("mode");

        name = (EditText) findViewById(R.id.editSerial2);
        regBtn = (Button) findViewById(R.id.profileRegisterBtn);

        if(mode.equals("add")){
            // 프로필 추가 모드
            System.out.println("프로필을 추가합니다.");
            regBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 추가할 프로필 이름,사진을 입력하고 등록버튼을 누르면 다시 프로필 선택 화면으로 돌아간다.
                    app.addProfileName(name.getText().toString());

                    Intent i = new Intent(getApplicationContext(), ProfileSelectActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }else{
            // 프로필 편집 모드
            index = i.getIntExtra("index", -1);
            System.out.println("프로필을 편집합니다.");
            profileName.setText(app.getProfileName(index));
            tv.setText("프로필을 편집합니다.");
            regBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 편집할 프로필 이름,사진을 입력하고 등록버튼을 누르면 다시 프로필 선택 화면으로 돌아간다.
                    app.editProfileName(name.getText().toString(), index);

                    Intent i = new Intent(getApplicationContext(), ProfileSelectActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }

        //


    }
}
