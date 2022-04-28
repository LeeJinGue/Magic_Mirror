package com.cookandroid.smartmirror.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;

public class ProfileSelectActivity extends AppCompatActivity {
    static LinearLayout[] pls;      //추가되는 profile LinearLayout 저장
    static int set_mode_check = 0;  //편집 or 이동모드 설정
    static int selectProfileNum = 0;    //선택한 profile 번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_select);

        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);

        // 변수들
        final int imgViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        final int imgViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        final GridLayout profileGridLayout = (GridLayout) findViewById(R.id.profileGrid);
        final ImageView setbtn = findViewById(R.id.profileSetting);
        final ImageView addbtn = findViewById(R.id.profileAdd);
        //DB에서 프로필 객체 수를 받아와서 크기 할당 필요
        pls = new LinearLayout[2];

        //DB에서 저장된 프로필 개수로 설정해야함
        for(int i=0; i<2; i++) {
            // 프로필 사진+프로필명 생성을 위한 LinLayout
            LinearLayout profile1 = new LinearLayout(this);
            LinearLayout.LayoutParams profileLinParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            profile1.setLayoutParams(profileLinParam);
            profile1.setGravity(Gravity.CENTER);
            profile1.setOrientation(LinearLayout.VERTICAL);

            // 프로필 사진(ImageView)
            ImageView profileImg = new ImageView(this);
            profileImg.setId(i+1);
            profileImg.setImageResource(R.drawable.ic_baseline_image_24);
            LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(imgViewWidth, imgViewHeight);
            profileImg.setLayoutParams(imgViewParams);
            profileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            // 프로필 명(TextView)
            TextView profileTextView = new TextView(this);
            LinearLayout.LayoutParams tViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            profileTextView.setLayoutParams(tViewParams);
            profileTextView.setText("프로필 "+i);
            profileTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            // Layout에 추가
            profile1.addView(profileImg);
            profile1.addView(profileTextView);

            // GridLayout에 추가
            profileGridLayout.addView(profile1);

            pls[i] = profile1;
        }
        //프로필추가 클릭 시
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfileNum = 0;
                Intent i = new Intent(getApplicationContext(), ProfileSettingActivity.class);
                startActivity(i);
                finish();
            }
        });

        //프로필 선택화면에서 click event로 기능 선택
        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfile();
            }
        });
    }
    void changeProfile() {
        if(set_mode_check == 0) {
            for(int i=0;i<2;i++) {
                ((ImageView)pls[i].findViewById(i+1)).setImageResource(R.drawable.ic_baseline_image_24);
                pls[i].findViewById(i+1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
            set_mode_check = 1;
        } else {
            for(int i=0;i<2;i++) {
                ((ImageView)pls[i].findViewById(i+1)).setImageResource(R.drawable.ic_baseline_offline_pin_24);
                pls[i].findViewById(i+1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectProfileNum = v.getId();
                        Intent i = new Intent(getApplicationContext(), ProfileSettingActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
            set_mode_check = 0;
        }

    }
}