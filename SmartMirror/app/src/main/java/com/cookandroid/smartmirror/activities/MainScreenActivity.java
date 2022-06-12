package com.cookandroid.smartmirror.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cookandroid.smartmirror.helper.MethodsHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.userData;
import com.cookandroid.smartmirror.navigationFragment.CheckBelongingSetFragment;
import com.cookandroid.smartmirror.navigationFragment.MessageSendFragment;
import com.cookandroid.smartmirror.navigationFragment.ScheduleFragment;
import com.cookandroid.smartmirror.navigationFragment.StockSetupFragment;
import com.cookandroid.smartmirror.navigationFragment.WindowSetupFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainScreenActivity extends AppCompatActivity {
    //하단 네비게이션 선택 시, 넘어갈 화면 변수
    WindowSetupFragment windowSetupFragment_frag;
    MessageSendFragment messageSendFragment_frag;
    ScheduleFragment scheduleFragment_frag;
    StockSetupFragment stockSetupFragment_frag;
    CheckBelongingSetFragment checkBelongingsFragment_frag;
    Toolbar toolbar;
    TextView tvTitle;
    userData selectedUser;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);


        MyApplication app = (MyApplication)getApplicationContext();
        selectedUser = app.getSelectedUser();
        Log.i("MainScreenActivity", selectedUser.getName()+"의 프로필이 선택되었습니다.");


        // Add Coustom AppBar & Set Title Color Gradient\
        toolbar = (Toolbar) findViewById(R.id.mainAppbar);

        tvTitle = (TextView) toolbar.findViewById(R.id.toolbarTv);
        MethodsHelper methodsHelper = new MethodsHelper();
        methodsHelper.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        /*  //Custom ActionBar - title_smartmirror.xml
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_smartmirror);
        */
        windowSetupFragment_frag = new WindowSetupFragment();
        messageSendFragment_frag = new MessageSendFragment();
        scheduleFragment_frag = new ScheduleFragment();
        stockSetupFragment_frag = new StockSetupFragment();
        checkBelongingsFragment_frag = new CheckBelongingSetFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, windowSetupFragment_frag).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab1:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, windowSetupFragment_frag).commit();
                                return true;
                            case R.id.tab2:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, messageSendFragment_frag).commit();
                                return true;
                            case R.id.tab3:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, scheduleFragment_frag).commit();
                                return true;
                            case R.id.tab4:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, stockSetupFragment_frag).commit();
                                return true;
                            case R.id.tab5:
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_profile_select,menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
//            case R.id.menu_settings:
//                Toast.makeText(this,"설정메뉴",Toast.LENGTH_SHORT).show();
//                break;
            case android.R.id.home:{
                finish();
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}