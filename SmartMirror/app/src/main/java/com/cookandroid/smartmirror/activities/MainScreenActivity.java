package com.cookandroid.smartmirror.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.navigationFragment.CheckBelongingsFragment;
import com.cookandroid.smartmirror.navigationFragment.MessageSendFragment;
import com.cookandroid.smartmirror.navigationFragment.ScheduleFragment;
import com.cookandroid.smartmirror.navigationFragment.StockSetupFragment;
import com.cookandroid.smartmirror.navigationFragment.WindowSetupFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreenActivity extends AppCompatActivity {
    //하단 네비게이션 선택 시, 넘어갈 화면 변수
    WindowSetupFragment windowSetupFragment_frag;
    MessageSendFragment messageSendFragment_frag;
    ScheduleFragment scheduleFragment_frag;
    StockSetupFragment stockSetupFragment_frag;
    CheckBelongingsFragment checkBelongingsFragment_frag;
    Toolbar toolbar;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        MyApplication app = (MyApplication)getApplicationContext();
        Log.i("메인스크린", app.getSelectedProfileName()+"의 프로필이 선택되었습니다.");
        // Add Coustom AppBar & Set Title Color Gradient\
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        tvTitle = (TextView) findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayShowTitleEnabled(false);
//        ab.setDisplayHomeAsUpEnabled(false);

        /*  //Custom ActionBar - title_smartmirror.xml
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_smartmirror);
        */
        windowSetupFragment_frag = new WindowSetupFragment();
        messageSendFragment_frag = new MessageSendFragment();
        scheduleFragment_frag = new ScheduleFragment();
        stockSetupFragment_frag = new StockSetupFragment();
        checkBelongingsFragment_frag = new CheckBelongingsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, windowSetupFragment_frag).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab1:
                                Toast.makeText(getApplicationContext(), "화면설정", Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, windowSetupFragment_frag).commit();

                                return true;

                            case R.id.tab2:
//                                Toast.makeText(getApplicationContext(), "메시지전송", Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, messageSendFragment_frag).commit();

                                return true;

                            case R.id.tab3:
                                Toast.makeText(getApplicationContext(), "일정등록", Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, scheduleFragment_frag).commit();

                                return true;

                            case R.id.tab4:
                                Toast.makeText(getApplicationContext(), "관심주설정", Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, stockSetupFragment_frag).commit();

                                return true;
                            case R.id.tab5:
                                Toast.makeText(getApplicationContext(), "소지품확인", Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, checkBelongingsFragment_frag).commit();

                                return true;
                        }

                        return false;
                    }
                }
        );

    }
    /*
    설정 아이콘 : https://www.flaticon.com/kr/search?word=%EC%84%A4%EC%A0%95&color=black&order_by=4&type=icon
    화면설정 아이콘 : https://www.flaticon.com/kr/free-icon/cellphone_1483193?related_id=1483193&origin=search
    메시지 아이콘 : https://www.flaticon.com/kr/free-icon/speech-bubble_5602855?related_id=5602855&origin=search
    일정 아이콘 : https://www.flaticon.com/kr/free-icon/schedule_6825897?related_id=6825897&origin=search
    주식 아이콘 : https://www.flaticon.com/kr/free-icon/stocks_1606566?related_id=1606566&origin=search
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_settings:
                Toast.makeText(this,"설정메뉴",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}